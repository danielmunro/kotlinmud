package kotlinmud.event.event

import kotlinmud.mob.Mob
import kotlinmud.mob.fight.Fight

data class FightStartedEvent(val fight: Fight, val aggressor: Mob, val defender: Mob)
