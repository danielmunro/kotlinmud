package kotlinmud.mob.fight

import kotlinmud.mob.MobEntity

class Round(
    val attacker: MobEntity,
    val defender: MobEntity,
    val attackerAttacks: List<Attack>,
    val defenderAttacks: List<Attack>
)
