package kotlinmud.world.itrias.fusil

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createFusilOutskirts(roomService: RoomService, connector: Room): Room {
    val roomBuilder = RoomBuilder(roomService).area(Area.FusilOutskirts)

    val room1 = roomBuilder.name("Outskirts of Fusil")
        .description("foo")
        .build()

    val room2 = roomBuilder.build()

    connect(connector).to(room1, Direction.NORTH)
        .to(room2, Direction.NORTH)

    return room2
}
