package kotlinmud.mob.fight

import kotlinmud.mob.dao.MobDAO

class Round(
    val attacker: MobDAO,
    val defender: MobDAO,
    val attackerAttacks: List<Attack>,
    val defenderAttacks: List<Attack>
) {
    fun isActive(): Boolean {
        return !attacker.isIncapacitated() && !defender.isIncapacitated()
    }

    fun getParticipants(): List<MobDAO> {
        return listOf(attacker, defender)
    }
}
