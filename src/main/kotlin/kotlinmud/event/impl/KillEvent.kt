package kotlinmud.event.impl

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight

class KillEvent(val fight: Fight, val victor: MobDAO, val vanquished: MobDAO)
