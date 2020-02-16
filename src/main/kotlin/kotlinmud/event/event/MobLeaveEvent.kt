package kotlinmud.event.event

import kotlinmud.mob.MobEntity
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity

class MobLeaveEvent(val mob: MobEntity, val room: RoomEntity, val direction: Direction)
