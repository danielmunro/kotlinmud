package kotlinmud.world.impl.itrias.troy.city.hauntedMansion

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createHauntedMansionTopFloor(areaBuilderService: AreaBuilderService, connection: Room) {
    areaBuilderService.startWith(connection)
        .buildRoom("top floor", Direction.UP) {
            it.name = "On a staircase"
            it.description = "tbd"
        }

    val topMatrix = SimpleMatrixService(
        areaBuilderService.roomBuilder(
            "A private study",
            "tbd",
        )
    ).build(3, 3)

    areaBuilderService.startWith("top floor")
        .connectTo(topMatrix[0][0], Direction.SOUTH)

    areaBuilderService.buildFodder(
        "a circle mansion guardian",
        "a circle mansion guardian is here, ready to defend his ancestral hall",
        "tbd",
        Human(),
        3,
        3,
    )
}
