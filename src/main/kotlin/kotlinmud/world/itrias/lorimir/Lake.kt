package kotlinmud.world.itrias.lorimir

import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createLorimirForestLake(roomService: RoomService, connection: Room) {
    val room = roomService.builder(
        "A path in the woods",
        "tbd",
        Area.LakeOsona,
    )

    val room1 = build(
        room.also {
            it.name = "A path in the woods"
            it.description = "foo"
        }
    )

    val room2 = build(room)
    val room3 = build(room)
    val room4 = build(
        room.also {
            it.name = "The trail begins to open up"
            it.description = "You can see the trail leading to an opening. A lake is in the distance."
        }
    )
    val room5 = build(
        room.also {
            it.name = "Lake at Lorimir Forest"
            it.description = "foo"
        }
    )

    connect(connection)
        .toRoom(room1, Direction.DOWN)
        .toRoom(room2, Direction.EAST)
        .toRoom(room3, Direction.EAST)
        .toRoom(room4, Direction.SOUTH)
        .toRoom(room5, Direction.SOUTH)
}
