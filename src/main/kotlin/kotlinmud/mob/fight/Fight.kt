package kotlinmud.mob.fight

import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.math.d20
import kotlinmud.helper.math.dN
import kotlinmud.helper.math.percentRoll
import kotlinmud.item.type.Position
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class Fight(private val mob1: MobDAO, private val mob2: MobDAO) {
    private var status: FightStatus = FightStatus.FIGHTING
    private val logger = LoggerFactory.getLogger(Fight::class.java)

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
        if (mob == mob1) {
            return mob2
        }
        if (mob == mob2) {
            return mob1
        }
        return null
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

    private fun rollEvasiveSkills(mob: MobDAO): SkillType? {
        return transaction {
//            val itemResult = Items.select { Items.mobEquippedId eq mob.id and (Items.position eq Position.SHIELD.toString()) }.singleOrNull()
//            val skillResult = Skills.select { Skills.mobId eq mob.id and (Skills.type eq SkillType.SHIELD_BLOCK.toString()) }.singleOrNull()
            mob.equipped.find { it.position == Position.SHIELD }?.let {
                mob.skills.find { it.type == SkillType.SHIELD_BLOCK }?.let {
                    if (percentRoll() < it.level / 3) {
                        return@transaction SkillType.SHIELD_BLOCK
                    }
                }
            }

            mob.equipped.find { it.position == Position.WEAPON }?.let { _ ->
                mob.skills.find { it.type == SkillType.PARRY }?.let {
                    if (percentRoll() < it.level / 3) {
                        return@transaction SkillType.PARRY
                    }
                }
            }

            mob.skills.find { it.type == SkillType.DODGE }?.let {
                if (percentRoll() < it.level / 3) {
                    return@transaction SkillType.DODGE
                }
            }

            return@transaction null
        }
    }

    private fun calculateDamage(attacker: MobDAO): Int {
        val hit = attacker.calc(Attribute.HIT)
        val dam = attacker.calc(Attribute.DAM)

        return dN(hit, dam) + dam
    }

    private fun attackerDefeatsDefenderAC(attacker: MobDAO, defender: MobDAO): Boolean {
        val roll = d20()
        val hit = attacker.calc(Attribute.HIT)
        val ac = getAc(defender, attacker.getDamageType())
        logger.debug("ac check :: roll: {}, hit: {}, ac: {}, final value: {}", roll, hit, ac, roll - hit + ac)
        return roll - hit + ac < 0
    }

    private fun getAc(defender: MobDAO, damageType: DamageType): Int {
        return when (damageType) {
            DamageType.SLASH -> defender.calc(Attribute.AC_SLASH)
            DamageType.POUND -> defender.calc(Attribute.AC_BASH)
            DamageType.PIERCE -> defender.calc(Attribute.AC_PIERCE)
            else -> defender.calc(Attribute.AC_MAGIC)
        }
    }
}
