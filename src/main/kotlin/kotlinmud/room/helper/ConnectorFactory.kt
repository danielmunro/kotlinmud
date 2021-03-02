package kotlinmud.room.helper

import kotlinmud.room.model.Room

fun connect(room: Room): RoomConnector {
    return RoomConnector(room)
}
