package kotlinmud.event

import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room

class MobMoveEvent(override val mob: Mob, override val room: Room, override val direction: Direction) : Event {
    override val eventType: EventType = EventType.MOB_MOVE
}