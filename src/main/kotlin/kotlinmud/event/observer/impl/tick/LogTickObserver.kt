package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.logger
import kotlinmud.io.service.ServerService

class LogTickObserver(private val serverService: ServerService) : Observer {
    private val logger = logger(this)

    override suspend fun <T> invokeAsync(event: Event<T>) {
        logger.info("tick :: {} clients, {} mobs", serverService.getClients().size)
    }
}
