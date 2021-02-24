package kotlinmud.event.impl

import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob

class KillEvent(val fight: Fight, val victor: Mob, val vanquished: Mob)
