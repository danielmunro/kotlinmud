package kotlinmud.world.impl.itrias.troy

import kotlinmud.mob.race.impl.Avian
import kotlinmud.mob.race.impl.Deer
import kotlinmud.mob.race.impl.Goat
import kotlinmud.mob.service.MobService
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyOutskirts(mobService: MobService, roomService: RoomService, connector: Room): Room {
    val roomBuilder = roomService.builder(
        "Outskirts of Troy",
        "Magnificent columns from a bygone era stand scattered over the landscape.",
        Area.Troy,
    )

    val endRoom = build(roomBuilder)

     mobService.buildFodder(
         "a billy goat",
         "a tan billy goat is here, grazing on grass",
         "tbd",
         Goat(),
         5,
         Area.Troy,
         3,
     )

    mobService.buildFodder(
        "a red tail hawk",
        "you have caught the attention of a formidable-looking hawk",
        "tbd",
        Avian(),
        7,
        Area.Troy,
        2,
    )

    mobService.buildFodder(
        "a spring fawn",
        "you catch a glimpse of a spring fawn darting past some ruins",
        "tbd",
        Deer(),
        3,
        Area.Troy,
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
