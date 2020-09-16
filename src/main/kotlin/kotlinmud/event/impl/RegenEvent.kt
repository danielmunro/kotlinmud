package kotlinmud.event.impl

import kotlinmud.mob.dao.MobDAO

class RegenEvent(
    val mob: MobDAO,
    var hpRegenRate: Double,
    var manaRegenRate: Double,
    var mvRegenRate: Double
)
