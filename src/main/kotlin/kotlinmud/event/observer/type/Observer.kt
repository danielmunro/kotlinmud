package kotlinmud.event.observer.type

import kotlinmud.event.impl.Event
import kotlinmud.event.type.EventType

interface Observer {
    val eventType: EventType
    fun <T> processEvent(event: Event<T>)
}
