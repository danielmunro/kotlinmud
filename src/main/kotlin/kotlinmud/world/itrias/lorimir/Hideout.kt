package kotlinmud.world.itrias.lorimir

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createGrongokHideout(roomService: RoomService, connector: Room) {
    val builder = RoomBuilder(roomService).also {
        it.name = "entrance to a cave"
        it.description = "a cave"
        it.area = Area.GrongokHideout
    }

    val room1 = builder.also { it.id = 200 }.build()
    val room2 = builder.also {
        it.id = 201
        it.name = "deep in a cave"
    }.build()
    val room3 = builder.also { it.id = 202 }.build()
    val room4 = builder.also { it.id = 203 }.build()
    val room5 = builder.also { it.id = 204 }.build()

    connect(connector).to(room1).to(room2) to mapOf(
        Pair(room3, Direction.SOUTH),
        Pair(room4, Direction.EAST),
        Pair(room5, Direction.WEST),
    )
}
