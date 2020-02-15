package kotlinmud.event

import kotlinmud.event.event.ClientConnectedEvent
import kotlinmud.event.event.MobMoveEvent
import kotlinmud.io.Syntax
import kotlinmud.mob.MobEntity
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity
import java.net.Socket

fun createClientConnectedEvent(socket: Socket): Event<ClientConnectedEvent> {
    return Event(EventType.CLIENT_CONNECTED, ClientConnectedEvent(socket))
}

fun createMobMoveEvent(mob: MobEntity, room: RoomEntity, direction: Direction): Event<MobMoveEvent> {
    return Event(EventType.MOB_MOVE, MobMoveEvent(mob, room, direction))
}
