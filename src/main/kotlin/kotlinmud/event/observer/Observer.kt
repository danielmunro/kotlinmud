package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType

interface Observer {
    val eventTypes: List<EventType>
    fun <T, A> processEvent(event: Event<T>): EventResponse<A>
}
