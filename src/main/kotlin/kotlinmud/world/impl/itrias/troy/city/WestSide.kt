package kotlinmud.world.impl.itrias.troy.city

import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.impl.itrias.troy.hauntedMansion.createTroyHauntedMansion
import kotlinmud.world.service.AreaBuilderService

fun createTroyWestSide(
    areaBuilderService: AreaBuilderService,
    connection: Room,
) {
    areaBuilderService
        .startWith(connection)
        .buildRoom("sunset1", Direction.WEST) {
            it.name = "Sunset Boulevard"
            it.description = "tbd"
        }.buildRoom(Direction.WEST)
        .buildRoom(Direction.WEST)
        .connectTo("west gate", Direction.WEST)

    createTroyHauntedMansion(
        areaBuilderService.copy(Area.HauntedMansion),
        areaBuilderService.getRoomFromLabel("sunset1"),
    )
}
