package kotlinmud.world.impl.itrias.troy

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Felid
import kotlinmud.mob.race.impl.Rabbit
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyPromenade(areaBuilderService: AreaBuilderService, connector: Room): Room {
    val builder = areaBuilderService.roomBuilder(
        "On The Promenade",
        "tbd",
    )

    val matrix = SimpleMatrixService(builder).build(5, 5)

    connect(connector).toRoom(matrix[2][4], Direction.UP)

    areaBuilderService.buildFodder(
        "a small rabbit",
        "a small brown rabbit is here, sniffing around for a quick snack",
        "tbd",
        Rabbit(),
        3,
        4,
    )

    areaBuilderService.buildFodder(
        "an alley cat",
        "a mangy alley cat is here, scrounging for scraps",
        "tbd",
        Felid(),
        4,
        2,
    )

    return matrix[2][0]
}
