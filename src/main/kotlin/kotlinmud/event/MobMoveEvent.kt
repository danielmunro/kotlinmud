package kotlinmud.event

import kotlinmud.mob.MobEntity
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity

class MobMoveEvent(override val mob: MobEntity, override val room: RoomEntity, override val direction: Direction) : Event {
    override val eventType: EventType = EventType.MOB_MOVE
}
