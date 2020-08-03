package kotlinmud.mob.fight

import kotlinmud.mob.dao.MobDAO

class Round(
    val fight: Fight,
    val attacker: MobDAO,
    val defender: MobDAO,
    val attackerAttacks: List<Attack>,
    val defenderAttacks: List<Attack>
) {
    fun isActive(): Boolean {
        return !fight.isOver()
    }

    fun getParticipants(): List<MobDAO> {
        return listOf(attacker, defender)
    }
}
