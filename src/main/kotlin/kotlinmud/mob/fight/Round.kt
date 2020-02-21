package kotlinmud.mob.fight

import kotlinmud.mob.Mob

class Round(
    val attacker: Mob,
    val defender: Mob,
    val attackerAttacks: List<Attack>,
    val defenderAttacks: List<Attack>
) {
    fun isActive(): Boolean {
        return !attacker.isIncapacitated() && !defender.isIncapacitated()
    }
}
