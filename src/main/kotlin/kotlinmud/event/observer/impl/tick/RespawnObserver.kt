package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.respawn.service.RespawnService

class RespawnObserver(private val respawnService: RespawnService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        respawnService.respawn()
    }
}
