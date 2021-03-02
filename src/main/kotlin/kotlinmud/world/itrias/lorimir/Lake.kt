package kotlinmud.world.itrias.lorimir

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createLorimirForestLake(roomService: RoomService, connection: Room) {
    val room = RoomBuilder(roomService).area(Area.LakeOsona)

    val room1 = room.name("A path in the woods")
        .description("foo")
        .build()

    val room2 = room.build()
    val room3 = room.build()
    val room4 = room.name("The trail begins to open up")
        .description("You can see the trail leading to an opening. A lake is in the distance.")
        .build()
    val room5 = room.name("Lake at Lorimir Forest")
        .description("foo")
        .build()

    connect(connection).to(room1, Direction.DOWN)
        .to(room2, Direction.EAST)
        .to(room3, Direction.EAST)
        .to(room4, Direction.SOUTH)
        .to(room5, Direction.SOUTH)
}
