package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.player.service.PlayerService

fun <T> clientConnectedEvent(playerService: PlayerService, event: Event<T>) {
    with(event.subject as ClientConnectedEvent) {
        playerService.addPreAuthClient(this.client)
        this.client.write("email: ")
    }
}
