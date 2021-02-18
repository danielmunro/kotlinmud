package kotlinmud.world.itrias.fusil

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createFusilOutskirts(connector: RoomDAO): RoomDAO {
    val roomBuilder = RoomBuilder().area(Area.FusilOutskirts)

    val room1 = roomBuilder.name("Outskirts of Fusil")
        .description("foo")
        .build()

    val room2 = roomBuilder.build()

    connect(connector).to(room1, Direction.NORTH)
        .to(room2, Direction.NORTH)

    return room2
}
