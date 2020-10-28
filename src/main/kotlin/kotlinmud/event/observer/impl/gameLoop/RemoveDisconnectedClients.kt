package kotlinmud.event.observer.impl.gameLoop

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.io.service.ServerService

class RemoveDisconnectedClients(private val serverService: ServerService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        serverService.removeDisconnectedClients()
    }
}
