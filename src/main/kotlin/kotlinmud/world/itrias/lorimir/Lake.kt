package kotlinmud.world.itrias.lorimir

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createLorimirForestLake(roomService: RoomService, connection: Room) {
    val room = RoomBuilder(roomService).also {
        it.area = Area.LakeOsona
    }

    val room1 = room.also {
        it.id = 300
        it.name = "A path in the woods"
        it.description = "foo"
    }.build()

    val room2 = room.also { it.id = 301 }.build()
    val room3 = room.also { it.id = 302 }.build()
    val room4 = room.also {
        it.id = 304
        it.name = "The trail begins to open up"
        it.description = "You can see the trail leading to an opening. A lake is in the distance."
    }.build()
    val room5 = room.also {
        it.id = 305
        it.name = "Lake at Lorimir Forest"
        it.description = "foo"
    }.build()

    connect(connection).to(room1, Direction.DOWN)
        .to(room2, Direction.EAST)
        .to(room3, Direction.EAST)
        .to(room4, Direction.SOUTH)
        .to(room5, Direction.SOUTH)
}
