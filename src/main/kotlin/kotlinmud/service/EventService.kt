package kotlinmud.service

import kotlinmud.event.Event
import kotlinmud.event.observer.Observer

class EventService(private val observers: Array<Observer>) {
    fun <T> publish(event: Event<T>) {
        observers.filter { it.eventTypes.contains(event.eventType) }
            .forEach { it.processEvent(event) }
    }
}
