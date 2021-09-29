package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService
import kotlinmud.world.type.AreaConnections

fun createSouthernTrail(svc: AreaBuilderService): AreaConnections {
    svc.buildRoom(Direction.SOUTH) {
        it.name = "Along a trail"
    }
        .buildRoom(Direction.SOUTH)
        .buildRoom("crossroads", Direction.SOUTH)
        .buildRoom(Direction.SOUTH)
        .buildRoom(Direction.SOUTH)

    return mapOf()
}
