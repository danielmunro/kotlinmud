package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.player.service.PlayerService

class ClientConnectedObserver(private val playerService: PlayerService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as ClientConnectedEvent) {
            playerService.addPreAuthClient(this.client)
            this.client.write("account name: ")
        }
    }
}
