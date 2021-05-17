package kotlinmud.room.builder

import kotlinmud.room.model.Room

var id = 0

fun build(roomBuilder: RoomBuilder): Room {
    id++
    roomBuilder.id = id
    return roomBuilder.build()
}
