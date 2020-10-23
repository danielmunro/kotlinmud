package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.service.ClientService

class DecrementDelayObserver(private val clientService: ClientService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        clientService.decrementDelays()
    }
}
