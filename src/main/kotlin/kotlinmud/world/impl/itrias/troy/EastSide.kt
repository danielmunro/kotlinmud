package kotlinmud.world.impl.itrias.troy

import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyEastSide(areaBuilderService: AreaBuilderService, connection: Room): Room {
    return areaBuilderService.startWith(connection)
        .buildRoom(Direction.EAST) {
            it.name = "Sunrise Avenue"
            it.name = "tbd"
        }
        .buildRoom(Direction.EAST)
        .buildRoom(Direction.EAST).lastRoom
}
