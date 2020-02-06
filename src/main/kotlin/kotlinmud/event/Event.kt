package kotlinmud.event

import kotlinmud.mob.Mob
import kotlinmud.room.Room

open class Event(val eventType: EventType, val mob: Mob, val room: Room) {
}