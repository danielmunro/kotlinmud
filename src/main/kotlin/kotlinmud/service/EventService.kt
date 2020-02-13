package kotlinmud.service

import kotlinmud.event.Event
import kotlinmud.event.observer.Observer

class EventService(private val observers: Array<Observer>) {
    fun publish(event: Event) {
        observers.filter { it.eventTypes.contains(event.eventType) }
            .forEach { it.processEvent(event) }
    }
}
