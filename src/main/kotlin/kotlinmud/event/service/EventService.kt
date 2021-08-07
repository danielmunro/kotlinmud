package kotlinmud.event.service

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.ObserverList

class EventService {
    lateinit var observers: ObserverList

    suspend fun <T> publish(event: Event<T>) {
        (observers[event.eventType] ?: return).forEach {
            it.invokeAsync(event)
        }
    }
}
