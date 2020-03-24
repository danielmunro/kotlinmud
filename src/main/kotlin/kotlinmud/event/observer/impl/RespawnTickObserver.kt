package kotlinmud.event.observer.impl

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.observer.Observer
import kotlinmud.service.RespawnService

class RespawnTickObserver(private val respawnService: RespawnService) :
    Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        respawnService.respawn()
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
