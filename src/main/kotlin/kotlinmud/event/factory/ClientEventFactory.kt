package kotlinmud.event.factory

import kotlinmud.event.impl.ClientConnectedEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PlayerLoggedInEvent
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Client
import kotlinmud.mob.model.PlayerMob

fun createClientDisconnectedEvent(client: Client): Event<Client> {
    return Event(
        EventType.CLIENT_DISCONNECTED,
        client
    )
}

fun createClientLoggedInEvent(client: Client, mob: PlayerMob): Event<PlayerLoggedInEvent> {
    return Event(
        EventType.CLIENT_LOGGED_IN,
        PlayerLoggedInEvent(client, mob)
    )
}

fun createClientConnectedEvent(client: Client): Event<ClientConnectedEvent> {
    return Event(
        EventType.CLIENT_CONNECTED,
        ClientConnectedEvent(client)
    )
}
