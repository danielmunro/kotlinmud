package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.persistence.service.StartupService

class RespawnObserver(private val startupService: StartupService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        startupService.respawn()
    }
}
