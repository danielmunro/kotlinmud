package kotlinmud.world.impl.itrias.troy

import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyWestSide(
    mobService: MobService,
    roomService: RoomService,
    itemService: ItemService,
    connection: Room,
) {
    val roomBuilder = roomService.builder(
        "Sunset Boulevard",
        "tbd",
        Area.Troy,
    )

    val room1 = build(roomBuilder)

    createTroyHauntedMansion(mobService, roomService, itemService, room1)

    connect(connection)
        .toRoom(room1, Direction.WEST)
        .toRoom(build(roomBuilder), Direction.WEST)
        .toRoom(build(roomBuilder), Direction.WEST)
}
