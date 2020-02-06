package kotlinmud.event.observer

import kotlinmud.event.Event
import kotlinmud.event.EventType

interface Observer {
    fun getEventTypes(): Array<EventType>
    fun processEvent(event: Event)
}