package kotlinmud.action

import kotlinmud.EventService
import kotlinmud.MobService
import kotlinmud.event.Event
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class ActionContextService(private val mobService: MobService, private val eventService: EventService) {
    fun getMobsInRoom(room: Room): List<Mob> {
        return mobService.getMobsForRoom(room)
    }

    fun publishEvent(event: Event) {
        eventService.publish(event)
    }
}