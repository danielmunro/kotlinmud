package kotlinmud.world.itrias.lorimir

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createGrongokHideout(roomService: RoomService, connector: Room) {
    val builder = RoomBuilder(roomService)
        .name("entrance to a cave")
        .description("a cave")
        .area(Area.GrongokHideout)

    val room1 = builder.build()
    val room2 = builder.name("deep in a cave").build()
    val room3 = builder.build()
    val room4 = builder.build()
    val room5 = builder.build()

    connect(connector).to(room1).to(room2) to mapOf(
        Pair(room3, Direction.SOUTH),
        Pair(room4, Direction.EAST),
        Pair(room5, Direction.WEST),
    )
}
