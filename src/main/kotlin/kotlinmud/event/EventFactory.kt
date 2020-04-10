package kotlinmud.event

import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.io.NIOClient
import kotlinmud.mob.Mob
import kotlinmud.world.room.Room

fun createClientConnectedEvent(client: NIOClient): Event<ClientConnectedEvent> {
    return Event(EventType.CLIENT_CONNECTED, ClientConnectedEvent(client))
}

fun createSendMessageToRoomEvent(message: Message, room: Room, actionCreator: Mob, target: Mob? = null): Event<SendMessageToRoomEvent> {
    return Event(EventType.SEND_MESSAGE_TO_ROOM, SendMessageToRoomEvent(message, room, actionCreator, target))
}

fun createClientDisconnectedEvent(client: NIOClient): Event<NIOClient> {
    return Event(EventType.CLIENT_DISCONNECTED, client)
}
