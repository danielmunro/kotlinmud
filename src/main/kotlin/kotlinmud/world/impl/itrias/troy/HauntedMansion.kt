package kotlinmud.world.impl.itrias.troy

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyHauntedMansion(mobService: MobService, roomService: RoomService, itemService: ItemService, connection: Room) {
    val roomBuilder = roomService.builder(
        "The Circle Hall Mansion",
        "tbd",
        Area.HauntedMansion,
    )

    val staircaseBuilder = roomBuilder.copy {
        it.name = "An ornate, winding staircase"
    }

    val foyer = build(roomBuilder.copy {
        it.name = "The foyer of the great Circle Hall"
    })

    val matrix = SimpleMatrixService(roomBuilder).build(3, 3)

    mobService.buildFodder(
        "a ghost",
        "a wandering ghost",
        "tbd",
        Human(),
        1,
        Area.HauntedMansion,
        6,
    )

    mobService.buildFodder(
        "a circle mansion guardian",
        "a circle mansion guardian is here, ready to defend his ancestral hall",
        "tbd",
        Human(),
        3,
        Area.HauntedMansionTopFloors,
        3,
    )

    connect(connection)
        .toRoom(foyer, Direction.NORTH)
        .toRoom(matrix[2][1], Direction.NORTH)

    val topFloorsBuilder = staircaseBuilder.copy {
        it.area = Area.HauntedMansionTopFloors
    }

    val secondFloor = build(topFloorsBuilder)
    val thirdFloor = build(topFloorsBuilder)
    val basement = staircaseBuilder.build()

    connect(matrix[1][1])
        .toRoom(
            listOf(
                Pair(secondFloor, Direction.UP),
                Pair(basement, Direction.DOWN),
            )
        )

    connect(secondFloor)
        .toRoom(thirdFloor, Direction.UP)
}
