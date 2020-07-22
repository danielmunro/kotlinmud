package kotlinmud.mob.fight

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.logger
import kotlinmud.helper.math.d20
import kotlinmud.helper.math.dN
import kotlinmud.helper.math.percentRoll
import kotlinmud.item.type.Position
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.fight.type.FightStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import org.jetbrains.exposed.sql.transactions.transaction

class Fight(private val mob1: MobDAO, private val mob2: MobDAO) {
    companion object {
        private fun getAc(defender: MobDAO, damageType: DamageType): Int {
            return when (damageType) {
                DamageType.SLASH -> defender.calc(Attribute.AC_SLASH)
                DamageType.POUND -> defender.calc(Attribute.AC_BASH)
                DamageType.PIERCE -> defender.calc(Attribute.AC_PIERCE)
                else -> defender.calc(Attribute.AC_MAGIC)
            }
        }

        private fun calculateDamage(attacker: MobDAO): Int {
            val hit = attacker.calc(Attribute.HIT)
            val dam = attacker.calc(Attribute.DAM)

            return dN(hit, dam) + dam
        }

        private fun rollEvasiveSkills(mob: MobDAO): SkillType? {
            val shield = mob.getEquippedByPosition(Position.SHIELD)
            val shieldBlock = mob.getSkill(SkillType.SHIELD_BLOCK)
            if (shield != null && shieldBlock != null && percentRoll() < transaction { shieldBlock.level } / 3) {
                return SkillType.SHIELD_BLOCK
            }

            val weapon = mob.getEquippedByPosition(Position.WEAPON)
            val parry = mob.getSkill(SkillType.PARRY)
            if (weapon != null && parry != null && percentRoll() < transaction { parry.level } / 3) {
                return SkillType.PARRY
            }

            val dodge = mob.getSkill(SkillType.DODGE)
            if (dodge != null && percentRoll() < transaction { dodge.level } / 3) {
                return SkillType.DODGE
            }

            return null
        }

        private fun resetDisposition(mob: MobDAO) {
            if (mob.disposition == Disposition.FIGHTING) {
                transaction {
                    mob.disposition = Disposition.STANDING
                }
            }
        }

        private fun applyRoundDamage(attacks: List<Attack>, mob: MobDAO) {
            transaction {
                attacks.forEach {
                    if (it.attackResult == AttackResult.HIT) {
                        mob.hp -= it.damage
                        mob.disposition = Disposition.FIGHTING
                    }
                }
                if (mob.hp < 0) {
                    mob.disposition = Disposition.DEAD
                }
            }
        }
    }

    private var status: FightStatus = FightStatus.FIGHTING
    private val logger = logger(this)

    init {
        transaction {
            mob1.disposition = Disposition.FIGHTING
            mob2.disposition = Disposition.FIGHTING
        }
    }

    fun isParticipant(mob: MobDAO): Boolean {
        return mob == mob1 || mob == mob2
    }

    fun getOpponentFor(mob: MobDAO): MobDAO? {
        return when (mob) {
            mob1 -> mob2
            mob2 -> mob1
            else -> null
        }
    }

    fun end() {
        status = FightStatus.OVER
        resetDisposition(mob1)
        resetDisposition(mob2)
    }

    fun isOver(): Boolean {
        return status == FightStatus.OVER
    }

    fun hasFatality(): Boolean {
        return mob1.isIncapacitated() || mob2.isIncapacitated()
    }

    fun getWinner(): MobDAO? {
        if (mob1.isIncapacitated()) {
            return mob2
        } else if (mob2.isIncapacitated()) {
            return mob1
        }
        return null
    }

    fun createRound(): Round {
        val round = Round(
            mob1,
            mob2,
            mapAttacks(mob1, mob2),
            mapAttacks(mob2, mob1)
        )
        applyRoundDamage(round.attackerAttacks, round.defender)
        if (round.isActive()) {
            applyRoundDamage(round.defenderAttacks, round.attacker)
        }
        if (!round.isActive()) {
            status = FightStatus.OVER
        }
        return round
    }

    private fun mapAttacks(attacker: MobDAO, defender: MobDAO): List<Attack> {
        return attacker.getAttacks().map {
            val skillType = rollEvasiveSkills(defender)
            when {
                skillType != null -> Attack(AttackResult.EVADE, attacker.getAttackVerb(), 0, mob1.getDamageType(), skillType)
                attackerDefeatsDefenderAC(attacker, defender) -> Attack(
                    AttackResult.HIT,
                    attacker.getAttackVerb(),
                    calculateDamage(attacker),
                    attacker.getDamageType()
                )
                else -> Attack(AttackResult.MISS, attacker.getAttackVerb(), 0, mob1.getDamageType())
            }
        }
    }

    private fun attackerDefeatsDefenderAC(attacker: MobDAO, defender: MobDAO): Boolean {
        val roll = d20()
        val hit = attacker.calc(Attribute.HIT)
        val ac = getAc(defender, attacker.getDamageType())
        logger.debug("ac check :: roll: {}, hit: {}, ac: {}, final value: {}", roll, hit, ac, roll - hit + ac)
        return roll - hit + ac < 0
    }
}
