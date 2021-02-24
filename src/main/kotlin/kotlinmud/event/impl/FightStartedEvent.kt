package kotlinmud.event.impl

import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob

data class FightStartedEvent(val fight: Fight, val aggressor: Mob, val defender: Mob)
