package kotlinmud.event

import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room

class MobMoveEvent(mob: Mob, room: Room, val direction: Direction) : Event(EventType.MOB_MOVE, mob, room) {

}