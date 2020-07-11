package kotlinmud.event.impl

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight

data class FightStartedEvent(val fight: Fight, val aggressor: MobDAO, val defender: MobDAO)
