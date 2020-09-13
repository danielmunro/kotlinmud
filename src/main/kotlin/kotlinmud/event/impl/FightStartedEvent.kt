package kotlinmud.event.impl

import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO

data class FightStartedEvent(val fight: FightDAO, val aggressor: MobDAO, val defender: MobDAO)
