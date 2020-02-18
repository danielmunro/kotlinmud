package kotlinmud.event.event

import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room

class MobLeaveEvent(val mob: Mob, val room: Room, val direction: Direction)
