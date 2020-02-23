package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.service.MobService

class DecrementDelayObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        mobService.decrementDelays()
        @Suppress("UNCHECKED_CAST")
        return EventResponse(event as A)
    }
}
