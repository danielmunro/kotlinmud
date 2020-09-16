package kotlinmud.event.impl

import kotlinmud.mob.dao.MobDAO

class RegenEvent(
    val mobDAO: MobDAO,
    var hpRegenRate: Double,
    var manaRegenRate: Double,
    var mvRegenRate: Double
)
