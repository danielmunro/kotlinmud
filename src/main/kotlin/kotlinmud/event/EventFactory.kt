package kotlinmud.event

import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.model.Client
import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Room

fun createClientConnectedEvent(client: Client): Event<ClientConnectedEvent> {
    return Event(EventType.CLIENT_CONNECTED, ClientConnectedEvent(client))
}

fun createSendMessageToRoomEvent(message: Message, room: Room, actionCreator: Mob, target: Mob? = null): Event<SendMessageToRoomEvent> {
    return Event(EventType.SEND_MESSAGE_TO_ROOM, SendMessageToRoomEvent(message, room, actionCreator, target))
}

fun createClientDisconnectedEvent(client: Client): Event<Client> {
    return Event(EventType.CLIENT_DISCONNECTED, client)
}
