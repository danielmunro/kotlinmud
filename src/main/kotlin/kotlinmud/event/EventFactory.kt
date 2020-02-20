package kotlinmud.event

import java.net.Socket
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.event.InputReceivedEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.ClientHandler
import kotlinmud.io.Message
import kotlinmud.mob.Mob
import kotlinmud.room.Room

fun createClientConnectedEvent(socket: Socket): Event<ClientConnectedEvent> {
    return Event(EventType.CLIENT_CONNECTED, ClientConnectedEvent(socket))
}

fun createSendMessageToRoomEvent(message: Message, room: Room, actionCreator: Mob, target: Mob? = null): Event<SendMessageToRoomEvent> {
    return Event(EventType.SEND_MESSAGE_TO_ROOM, SendMessageToRoomEvent(message, room, actionCreator, target))
}

fun createInputReceivedEvent(client: ClientHandler): Event<InputReceivedEvent> {
    return Event(EventType.INPUT_RECEIVED, InputReceivedEvent(client))
}
