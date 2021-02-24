package kotlinmud.event.impl

import kotlinmud.mob.model.Mob

class RegenEvent(
    val mob: Mob,
    var hpRegenRate: Double,
    var manaRegenRate: Double,
    var mvRegenRate: Double
)
