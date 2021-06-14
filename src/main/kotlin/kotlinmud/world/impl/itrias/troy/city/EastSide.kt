package kotlinmud.world.impl.itrias.troy.city

import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyEastSide(areaBuilderService: AreaBuilderService, connection: Room) {
    areaBuilderService.startWith(connection)
        .buildRoom(Direction.EAST) {
            it.name = "Sunrise Avenue"
        }
        .buildRoom(Direction.EAST)
        .buildRoom(Direction.EAST)
        .connectTo("east gate", Direction.EAST)
}
