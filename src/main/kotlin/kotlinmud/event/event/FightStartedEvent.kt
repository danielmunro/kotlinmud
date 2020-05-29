package kotlinmud.event.event

import kotlinmud.mob.fight.Fight
import kotlinmud.mob.model.Mob

data class FightStartedEvent(val fight: Fight, val aggressor: Mob, val defender: Mob)
