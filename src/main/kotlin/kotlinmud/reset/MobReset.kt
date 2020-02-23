package kotlinmud.reset

import kotlinmud.mob.Mob
import kotlinmud.room.Room

class MobReset(val mob: Mob, val room: Room, val maxInRoom: Int, val maxInWorld: Int)