package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventType

interface Observer {
    val eventTypes: Array<EventType>
    fun <T> processEvent(event: Event<T>)
}
