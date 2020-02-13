package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventType

interface Observer {
    val eventTypes: Array<EventType>
    fun processEvent(event: Event)
}
