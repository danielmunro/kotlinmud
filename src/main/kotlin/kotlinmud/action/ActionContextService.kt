package kotlinmud.action

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.mob.MobEntity
import kotlinmud.room.RoomEntity
import kotlinmud.service.EventService
import kotlinmud.service.MobService

class ActionContextService(private val mobService: MobService, private val eventService: EventService) {
    fun getMobsInRoom(room: RoomEntity): List<MobEntity> {
        return mobService.getMobsForRoom(room)
    }

    fun <T, A> publishEvent(event: Event<T>): EventResponse<A> {
        return eventService.publish(event)
    }
}
