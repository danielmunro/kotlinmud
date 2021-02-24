package kotlinmud.mob.fight

import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.type.Disposition

class Round(
    val fight: Fight,
    val attacker: Mob,
    val defender: Mob,
    val attackerAttacks: MutableList<Attack>,
    val defenderAttacks: MutableList<Attack>
) {
    fun isActive(): Boolean {
        return !fight.isOver()
    }

    fun getParticipants(): List<Mob> {
        return listOf(attacker, defender)
    }

    fun hasFatality(): Boolean {
        return attacker.disposition == Disposition.DEAD || defender.disposition == Disposition.DEAD
    }
}
