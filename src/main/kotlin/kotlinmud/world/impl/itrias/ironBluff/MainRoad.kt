package kotlinmud.world.impl.itrias.ironBluff

import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createIronBluffMainRoad(areaBuilderService: AreaBuilderService) {
    areaBuilderService.buildRoom(Direction.WEST) {
        it.name = "A well-worn cobblestone road"
    }

    createPyreforgeFarm(areaBuilderService.copy(Area.PyreforgeFarm))
}
