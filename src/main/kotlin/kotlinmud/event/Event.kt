package kotlinmud.event

import kotlinmud.mob.MobEntity
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity

interface Event {
    val eventType: EventType
    val mob: MobEntity
    val room: RoomEntity
    val direction: Direction
}