package kotlinmud.world.impl.itrias.troy

import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.impl.itrias.troy.hauntedMansion.createTroyHauntedMansion
import kotlinmud.world.service.AreaBuilderService

fun createTroyWestSide(
    areaBuilderService: AreaBuilderService,
    connection: Room,
): Room {
    val room = areaBuilderService
        .startWith(connection)
        .buildRoom("sunset1", Direction.WEST) {
            it.name = "Sunset Boulevard"
            it.description = "tbd"
        }.lastRoom
    val exit = areaBuilderService
        .buildRoom(Direction.WEST)
        .buildRoom(Direction.WEST)
        .lastRoom

    createTroyHauntedMansion(areaBuilderService.copy(Area.HauntedMansion), room)

    return exit
}
