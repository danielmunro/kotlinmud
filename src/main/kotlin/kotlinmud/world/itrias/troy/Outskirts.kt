package kotlinmud.world.itrias.troy

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyOutskirts(roomService: RoomService, connector: Room): Room {
    val roomBuilder = RoomBuilder(roomService).also {
        it.area = Area.Troy
        it.name = "Outskirts of Troy"
        it.description = "Magnificent columns from a bygone era stand scattered over the landscape."
    }

    val endRoom = build(roomBuilder)

    connect(connector)
        .toRoom(build(roomBuilder), Direction.NORTH)
        .toRoom(build(roomBuilder), Direction.WEST)
        .toRoom(build(roomBuilder), Direction.UP)
        .toRoom(build(roomBuilder), Direction.NORTH)
        .toRoom(endRoom, Direction.WEST)

    return endRoom
}
