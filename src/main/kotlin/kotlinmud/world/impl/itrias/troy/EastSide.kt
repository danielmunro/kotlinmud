package kotlinmud.world.impl.itrias.troy

import kotlinmud.mob.service.MobService
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyEastSide(mobService: MobService, roomService: RoomService, connection: Room) {
    val roomBuilder = roomService.builder(
        "Sunrise Avenue",
        "A road",
        Area.Troy,
    )

    val road1 = build(roomBuilder)
    val road2 = build(roomBuilder)
    val road3 = build(roomBuilder)

    val gate = build(
        roomBuilder.copy {
            it.name = "Troy East Gate"
        }
    )

    connect(connection)
        .toRoom(road1, Direction.EAST)
        .toRoom(road2, Direction.EAST)
        .toRoom(road3, Direction.EAST)
        .toRoom(gate, Direction.EAST)
}
