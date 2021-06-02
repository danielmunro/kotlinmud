package kotlinmud.world.impl.itrias.troy

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.mob.race.impl.Felid
import kotlinmud.mob.race.impl.Rabbit
import kotlinmud.mob.service.MobService
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyPromenade(roomService: RoomService, mobService: MobService, connector: Room): Room {
    val builder = roomService.builder(
        "On The Promenade",
        "tbd",
        Area.TroyPromenade,
    )

    val matrix = SimpleMatrixService(builder).build(5, 5)

    connect(connector).toRoom(matrix[2][4], Direction.UP)

    mobService.buildFodder(
        "a small rabbit",
        "a small brown rabbit is here, sniffing around for a quick snack",
        "tbd",
        Rabbit(),
        3,
        Area.TroyPromenade,
        4,
    )

    mobService.buildFodder(
        "an alley cat",
        "a mangy alley cat is here, scrounging for scraps",
        "tbd",
        Felid(),
        4,
        Area.TroyPromenade,
        2,
    )

    return matrix[2][0]
}
