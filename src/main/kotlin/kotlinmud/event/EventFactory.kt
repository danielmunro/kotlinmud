package kotlinmud.event

import java.net.Socket
import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.event.MobLeaveEvent
import kotlinmud.event.event.SendMessageToRoomEvent
import kotlinmud.io.Message
import kotlinmud.mob.MobEntity
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity

fun createClientConnectedEvent(socket: Socket): Event<ClientConnectedEvent> {
    return Event(EventType.CLIENT_CONNECTED, ClientConnectedEvent(socket))
}

fun createMobLeaveRoomEvent(mob: MobEntity, room: RoomEntity, direction: Direction): Event<MobLeaveEvent> {
    return Event(EventType.MOB_LEAVE_ROOM, MobLeaveEvent(mob, room, direction))
}

fun createSendMessageToRoomEvent(message: Message, room: RoomEntity, actionCreator: MobEntity, target: MobEntity?): Event<SendMessageToRoomEvent> {
    return Event(EventType.SEND_MESSAGE_TO_ROOM, SendMessageToRoomEvent(message, room, actionCreator, target))
}
