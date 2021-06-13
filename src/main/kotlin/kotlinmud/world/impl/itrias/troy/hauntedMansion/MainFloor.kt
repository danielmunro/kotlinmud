package kotlinmud.world.impl.itrias.troy.hauntedMansion

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Human
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyHauntedMansion(areaBuilderService: AreaBuilderService, connection: Room) {
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

    createHauntedMansionTopFloor(areaBuilderService.copy(Area.HauntedMansionTopFloors), matrix[1][1])
    createHauntedMansionBasement(areaBuilderService.copy(Area.HauntedMansionBasement), matrix[1][1])
}
