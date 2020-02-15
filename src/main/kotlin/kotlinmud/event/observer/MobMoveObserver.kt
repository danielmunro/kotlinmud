package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.event.MobMoveEvent
import kotlinmud.event.response.MobMoveResponse
import kotlinmud.service.MobService

class MobMoveObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.MOB_MOVE)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        if (event.subject is MobMoveEvent) {
            mobService.moveMob(event.subject.mob, event.subject.room)
        }
        return MobMoveResponse(event as A)
    }
}
