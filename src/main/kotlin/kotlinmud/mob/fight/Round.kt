package kotlinmud.mob.fight

import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.type.Disposition

class Round(
    val fight: FightDAO,
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

    fun hasFatality(): Boolean {
        return attacker.disposition == Disposition.DEAD || defender.disposition == Disposition.DEAD
    }
}
