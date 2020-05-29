package kotlinmud.mob.fight

import kotlinmud.attributes.Attribute
import kotlinmud.item.Position
import kotlinmud.math.d20
import kotlinmud.math.dN
import kotlinmud.math.percentRoll
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.SkillType
import kotlinmud.mob.type.Disposition
import org.slf4j.LoggerFactory

class Fight(private val mob1: Mob, private val mob2: Mob) {
    private var status: FightStatus = FightStatus.FIGHTING
    private val logger = LoggerFactory.getLogger(Fight::class.java)

    init {
        mob1.disposition = Disposition.FIGHTING
        mob2.disposition = Disposition.FIGHTING
    }

    fun isParticipant(mob: Mob): Boolean {
        return mob == mob1 || mob == mob2
    }

    fun getOpponentFor(mob: Mob): Mob? {
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

    fun getWinner(): Mob? {
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

    private fun resetDisposition(mob: Mob) {
        if (mob.disposition == Disposition.FIGHTING) {
            mob.disposition = Disposition.STANDING
        }
    }

    private fun applyRoundDamage(attacks: List<Attack>, mob: Mob) {
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

    private fun mapAttacks(attacker: Mob, defender: Mob): List<Attack> {
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

    private fun rollEvasiveSkills(defender: Mob): SkillType? {
        defender.equipped.find { it.position == Position.SHIELD }?.let { _ ->
            defender.skills[SkillType.SHIELD_BLOCK]?.let {
                if (percentRoll() < it / 3) {
                    return SkillType.SHIELD_BLOCK
                }
            }
        }

        defender.equipped.find { it.position == Position.WEAPON }?.let { _ ->
            defender.skills[SkillType.PARRY]?.let {
                if (percentRoll() < it / 3) {
                    return SkillType.PARRY
                }
            }
        }

        defender.skills[SkillType.DODGE]?.let {
            if (percentRoll() < it / 3) {
                return SkillType.DODGE
            }
        }
        return null
    }

    private fun calculateDamage(attacker: Mob): Int {
        val hit = attacker.calc(Attribute.HIT)
        val dam = attacker.calc(Attribute.DAM)

        return dN(hit, dam) + dam
    }

    private fun attackerDefeatsDefenderAC(attacker: Mob, defender: Mob): Boolean {
        val roll = d20()
        val hit = attacker.calc(Attribute.HIT)
        val ac = getAc(defender, attacker.getDamageType())
        logger.debug("ac check :: roll: {}, hit: {}, ac: {}, final value: {}", roll, hit, ac, roll - hit + ac)
        return roll - hit + ac < 0
    }

    private fun getAc(defender: Mob, damageType: DamageType): Int {
        return when (damageType) {
            DamageType.SLASH -> defender.calc(Attribute.AC_SLASH)
            DamageType.POUND -> defender.calc(Attribute.AC_BASH)
            DamageType.PIERCE -> defender.calc(Attribute.AC_PIERCE)
            else -> defender.calc(Attribute.AC_MAGIC)
        }
    }
}
