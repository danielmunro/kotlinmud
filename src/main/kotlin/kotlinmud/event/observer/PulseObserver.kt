package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.event.Pulse

class PulseObserver : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.PULSE)
    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        println("pulse")
        @Suppress("UNCHECKED_CAST")
        return EventResponse(Pulse() as A)
    }
}
