package kotlinmud.event.service

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.ObserverList
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

class EventService {
    lateinit var observers: ObserverList

    suspend fun <T> publish(event: Event<T>) {
        (observers[event.eventType] ?: return).map {
            it.invokeAsync(event)
        }.asFlow().collect()
    }
}
