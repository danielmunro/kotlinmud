package kotlinmud.event.observer.impl.client

import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.player.service.PlayerService

class ClientConnectedObserver(private val playerService: PlayerService) : Observer {
    override val eventType = EventType.CLIENT_CONNECTED

    override fun <T> processEvent(event: Event<T>) {
        val connectedEvent = event.subject as ClientConnectedEvent
        val client = connectedEvent.client
        addPreAuthClient(client)
        client.write("email: ")
    }

    private fun addPreAuthClient(client: Client) {
        playerService.addPreAuthClient(client)
    }
}
