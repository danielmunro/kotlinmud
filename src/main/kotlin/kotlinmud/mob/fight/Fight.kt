package kotlinmud.mob.fight

import kotlinmud.attributes.Attribute
import kotlinmud.math.random.d20
import kotlinmud.math.random.dN
import kotlinmud.math.random.percentRoll
import kotlinmud.mob.Disposition
import kotlinmud.mob.Mob
import kotlinmud.mob.skill.SkillType

class Fight(private val mob1: Mob, private val mob2: Mob) {
    private var status: FightStatus = FightStatus.FIGHTING

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

    private fun applyRoundDamage(attacks: List<Attack>, mob: Mob) {
        attacks.forEach {
            if (it.attackResult == AttackResult.HIT) {
                mob.hp -= it.damage
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
                skillType != null -> Attack(AttackResult.EVADE, 0, mob1.getDamageType(), skillType)
                attackerDefeatsDefenderAC(attacker, defender) -> Attack(
                    AttackResult.HIT,
                    calculateDamage(attacker),
                    attacker.getDamageType()
                )
                else -> Attack(AttackResult.MISS, 0, mob1.getDamageType())
            }
        }
    }

    private fun rollEvasiveSkills(defender: Mob): SkillType? {
        defender.getEvasiveSkills().forEach { skillType ->
            defender.skills.getOrDefault(skillType, 1).let {
                if (percentRoll() < it / 3) {
                    return skillType
                }
            }
        }
        return null
    }

    private fun calculateDamage(attacker: Mob): Int {
        // hardcoded value for now, replace with weapon values
        return dN(1, 5) + attacker.calc(Attribute.DAM)
    }

    private fun attackerDefeatsDefenderAC(attacker: Mob, defender: Mob): Boolean {
        val roll = d20()
        val hit = attacker.calc(Attribute.HIT)
        val ac = getAc(defender, attacker.getDamageType())
        println("ac check with roll: $roll, hit: $hit, ac: $ac, final value: ${roll - hit + ac}")
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
