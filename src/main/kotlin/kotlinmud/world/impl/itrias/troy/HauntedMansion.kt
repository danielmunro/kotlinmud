package kotlinmud.world.impl.itrias.troy

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.builder.build
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyHauntedMansion(areaBuilderService: AreaBuilderService, connection: Room) {
    areaBuilderService
        .createNewArea(Area.HauntedMansion)
        .startWith(connection)
        .buildRoom(Direction.NORTH) {
            it.name = "The foyer of the great Circle Hall"
            it.description = "tbd"
        }

    val matrix = SimpleMatrixService(
        areaBuilderService.roomBuilder(
            "The Circle Hall Mansion",
            "tbd",
        )
    ).build(3, 3)

    areaBuilderService.connectTo(matrix[2][1], Direction.NORTH)

    areaBuilderService.buildFodder(
        "a ghost",
        "a wandering ghost",
        "tbd",
        Human(),
        1,
        6,
    )

//    areaBuilderService.buildFodder(
//        "a circle mansion guardian",
//        "a circle mansion guardian is here, ready to defend his ancestral hall",
//        "tbd",
//        Human(),
//        3,
//        3,
//    )

//    val topFloorsBuilder = staircaseBuilder.copy {
//        it.area = Area.HauntedMansionTopFloors
//    }
//
//    val secondFloor = build(topFloorsBuilder)
//    val thirdFloor = build(topFloorsBuilder)
//    val basement = staircaseBuilder.build()
//
//    connect(matrix[1][1])
//        .toRoom(
//            listOf(
//                Pair(secondFloor, Direction.UP),
//                Pair(basement, Direction.DOWN),
//            )
//        )
//
//    connect(secondFloor)
//        .toRoom(thirdFloor, Direction.UP)
}
