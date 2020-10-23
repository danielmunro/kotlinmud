package kotlinmud.event.observer.type

import kotlinmud.event.impl.Event

interface Observer {
    suspend fun <T> invokeAsync(event: Event<T>)
}
