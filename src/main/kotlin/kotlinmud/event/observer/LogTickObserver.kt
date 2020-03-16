package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.Tick

class LogTickObserver : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)
    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        println("tick")
        @Suppress("UNCHECKED_CAST")
        return EventResponse(Tick() as A)
    }
}
