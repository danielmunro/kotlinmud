package kotlinmud.world.impl.itrias.troy.hauntedMansion

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createHauntedMansionBasement(areaBuilderService: AreaBuilderService, connection: Room) {
    areaBuilderService.startWith(connection)
        .buildRoom(Direction.DOWN) {
            it.name = "On a staircase"
            it.description = "tbd"
        }
        .buildRoom(Direction.DOWN)

    val basementMatrix = SimpleMatrixService(
        areaBuilderService.roomBuilder(
            "A dark and creepy basement",
            "tbd",
        )
    ).build(5, 5)

    areaBuilderService
        .startWith(connection)
        .connectTo(basementMatrix[2][2], Direction.DOWN)

    areaBuilderService.buildFodder(
        "a tortured soul",
        "a tortured soul is here, wandering the circle hall basement",
        "tbd",
        Human(),
        3,
        3,
    )
}
