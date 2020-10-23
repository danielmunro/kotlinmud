package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer

class GrowResourcesObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        TODO("Not yet implemented")
    }
}