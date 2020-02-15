package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventType
import kotlinmud.event.EventResponse

interface Observer {
    val eventTypes: List<EventType>
    fun <T, A> processEvent(event: Event<T>): EventResponse<A>
}
