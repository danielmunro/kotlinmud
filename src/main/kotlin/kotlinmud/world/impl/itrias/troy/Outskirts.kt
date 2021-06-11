package kotlinmud.world.impl.itrias.troy

import kotlinmud.mob.race.impl.Avian
import kotlinmud.mob.race.impl.Deer
import kotlinmud.mob.race.impl.Goat
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.world.service.AreaBuilderService

fun createTroyOutskirts(areaBuilderService: AreaBuilderService, connector: Room): Room {
    val roomBuilder = areaBuilderService.roomBuilder(
        "Outskirts of Troy",
        "Magnificent columns from a bygone era stand scattered over the landscape.",
    )

    val endRoom = areaBuilderService.buildRoom("end").lastRoom

    areaBuilderService.buildFodder(
        "a billy goat",
        "a tan billy goat is here, grazing on grass",
        "tbd",
        Goat(),
        3,
        3,
    )

    areaBuilderService.buildFodder(
        "a red tail hawk",
        "you have caught the attention of a formidable-looking hawk",
        "tbd",
        Avian(),
        5,
        2,
    )

    areaBuilderService.buildFodder(
        "a spring fawn",
        "you catch a glimpse of a spring fawn darting past some ruins",
        "tbd",
        Deer(),
        2,
        3,
    )

    connect(connector)
        .toRoom(build(roomBuilder), Direction.NORTH)
        .toRoom(build(roomBuilder), Direction.NORTH)
        .toRoom(build(roomBuilder), Direction.NORTH)
        .toRoom(build(roomBuilder), Direction.WEST)
        .toRoom(endRoom, Direction.WEST)

    return endRoom
}
