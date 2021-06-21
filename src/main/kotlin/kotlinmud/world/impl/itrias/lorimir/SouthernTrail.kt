package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createSouthernTrail(svc: AreaBuilderService): AreaBuilderService {
    svc.buildRoom(Direction.SOUTH) {
        it.name = "Along a trail"
    }
        .buildRoom(Direction.SOUTH)
        .buildRoom("crossroads", Direction.SOUTH)
        .buildRoom(Direction.SOUTH)
        .buildRoom(Direction.SOUTH)

    return svc
}
