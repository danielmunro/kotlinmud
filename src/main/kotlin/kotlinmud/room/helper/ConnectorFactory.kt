package kotlinmud.room.helper

import kotlinmud.room.dao.RoomDAO

fun connect(room: RoomDAO): RoomConnector {
    return RoomConnector(room)
}
