package kotlinmud.world.impl.itrias.troy

import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyOutskirts(roomService: RoomService, connector: Room): Room {
    val roomBuilder = roomService.builder(
        "Outskirts of Troy",
        "Magnificent columns from a bygone era stand scattered over the landscape.",
        Area.Troy,
    )

    val endRoom = build(roomBuilder)

    connect(connector)
        .toRoom(build(roomBuilder), Direction.NORTH)
        .toRoom(build(roomBuilder), Direction.WEST)
        .toRoom(build(roomBuilder), Direction.UP)
        .toRoom(build(roomBuilder), Direction.NORTH)
        .toRoom(endRoom, Direction.WEST)

    return endRoom
}
