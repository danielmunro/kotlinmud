package kotlinmud.event

import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room

interface Event {
    val eventType: EventType
    val mob: Mob
    val room: Room
    val direction: Direction
}