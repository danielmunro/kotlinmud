package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.player.service.PlayerService

class ClientConnectedObserver(private val playerService: PlayerService) : Observer {
    override val eventType = EventType.CLIENT_CONNECTED

    override fun <T> processEvent(event: Event<T>) {
        with(event.subject as ClientConnectedEvent) {
            playerService.addPreAuthClient(this.client)
            this.client.write("email: ")
        }
    }
}
