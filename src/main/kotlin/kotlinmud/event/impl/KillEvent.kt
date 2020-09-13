package kotlinmud.event.impl

import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO

class KillEvent(val fight: FightDAO, val victor: MobDAO, val vanquished: MobDAO)
