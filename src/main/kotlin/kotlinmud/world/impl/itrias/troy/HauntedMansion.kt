package kotlinmud.world.impl.itrias.troy

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyHauntedMansion(initialBuilderService: AreaBuilderService, connection: Room) {
    val areaBuilderService = initialBuilderService.copy(Area.HauntedMansion)

    areaBuilderService.startWith(connection)
        .buildRoom("foyer", Direction.NORTH) {
            it.name = "The foyer of the great Circle Hall"
            it.description = "tbd"
        }

    val matrix = SimpleMatrixService(
        areaBuilderService.roomBuilder(
            "The Circle Hall Mansion",
            "tbd",
        )
    ).build(3, 3)

    areaBuilderService.startWith("foyer")
        .connectTo(matrix[2][1], Direction.NORTH)

    areaBuilderService.buildFodder(
        "a ghost",
        "a wandering ghost",
        "tbd",
        Human(),
        1,
        6,
    )

    val topFloorsAreaBuilderService = areaBuilderService.copy(Area.HauntedMansionTopFloors)

    topFloorsAreaBuilderService.startWith(matrix[1][1])
        .buildRoom("top floor", Direction.UP) {
            it.name = "On a staircase"
            it.description = "tbd"
        }

    val topMatrix = SimpleMatrixService(
        topFloorsAreaBuilderService.roomBuilder(
            "A private study",
            "tbd",
        )
    ).build(3, 3)

    topFloorsAreaBuilderService.startWith("top floor")
        .connectTo(topMatrix[0][0], Direction.SOUTH)

    topFloorsAreaBuilderService.buildFodder(
        "a circle mansion guardian",
        "a circle mansion guardian is here, ready to defend his ancestral hall",
        "tbd",
        Human(),
        3,
        3,
    )

    val basementAreaBuilder = areaBuilderService.copy(Area.HauntedMansionBasement)

    basementAreaBuilder.startWith(matrix[1][1])
        .buildRoom(Direction.DOWN) {
            it.name = "On a staircase"
            it.description = "tbd"
        }
        .buildRoom(Direction.DOWN)

    val basementMatrix = SimpleMatrixService(
        basementAreaBuilder.roomBuilder(
            "A dark and creepy basement",
            "tbd",
        )
    ).build(5, 5)

    basementAreaBuilder
        .startWith(matrix[1][1])
        .connectTo(basementMatrix[2][2], Direction.DOWN)

    basementAreaBuilder.buildFodder(
        "a tortured soul",
        "a tortured soul is here, wandering the circle hall basement",
        "tbd",
        Human(),
        3,
        3,
    )
}
