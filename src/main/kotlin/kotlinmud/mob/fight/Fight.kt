package kotlinmud.mob.fight

import kotlinmud.attributes.Attribute
import kotlinmud.mob.Mob
import kotlinmud.random.d20
import kotlinmud.random.dN

class Fight(private val mob1: Mob, private val mob2: Mob) {
    private var status: FightStatus = FightStatus.FIGHTING

    fun isOver(): Boolean {
        return status == FightStatus.OVER
    }

    fun createRound(): Round {
        val round = Round(
            mob1,
            mob2,
            mapAttacks(mob1, mob2),
            mapAttacks(mob2, mob1)
        )
        if (mob1.isIncapacitated() || mob2.isIncapacitated()) {
            status = FightStatus.OVER
        }
        return round
    }

    private fun mapAttacks(attacker: Mob, defender: Mob): List<Attack> {
        return attacker.getAttacks().map {
            if (attackerDefeatsDefenderAC(attacker, defender)) {
                Attack(
                    AttackResult.HIT,
                    calculateDamage(attacker),
                    attacker.getDamageType()
                )
            } else Attack(AttackResult.MISS, 0, mob1.getDamageType())
        }
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
