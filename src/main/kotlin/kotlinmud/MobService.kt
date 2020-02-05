package kotlinmud

import kotlinmud.mob.Mob
import kotlinmud.mob.MobRoom

class MobService {
    private val mobs: Array<Mob> = arrayOf()
    private val mobRooms: MutableList<MobRoom> = mutableListOf()
}