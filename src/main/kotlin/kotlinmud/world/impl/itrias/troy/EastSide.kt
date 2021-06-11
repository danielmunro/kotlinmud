package kotlinmud.world.impl.itrias.troy

import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyEastSide(areaBuilderService: AreaBuilderService, connection: Room): Room {
    val roomBuilder = areaBuilderService.roomBuilder(
        "Sunrise Avenue",
        "A road",
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

    return gate
}
