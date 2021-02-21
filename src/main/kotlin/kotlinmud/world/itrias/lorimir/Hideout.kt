package kotlinmud.world.itrias.lorimir

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createGrongokHideout(connector: RoomDAO) {
    val builder = RoomBuilder()
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
