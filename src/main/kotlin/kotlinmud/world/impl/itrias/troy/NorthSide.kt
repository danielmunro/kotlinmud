package kotlinmud.world.impl.itrias.troy

import kotlinmud.item.service.ItemService
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Kender
import kotlinmud.mob.race.impl.Lasher
import kotlinmud.mob.service.MobService
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createTroyNorthSide(
    mobService: MobService,
    roomService: RoomService,
    itemService: ItemService,
    connection: Room,
) {
    val pikeStreetBuilder = roomService.builder(
        "Pike Street",
        "tbd",
        Area.Troy,
    )

    val road1 = build(pikeStreetBuilder)
    val road2 = build(pikeStreetBuilder)
    val road3 = build(pikeStreetBuilder)

    val weaponShop = build(pikeStreetBuilder.copy {
        it.name = "Troy weapon smith shop"
    })

    mobService.buildShopkeeper(
        "the weaponsmith",
        "the weaponsmith is here",
        "tbd",
        Kender(),
        weaponShop,
        listOf(),
    )

    val armorShop = build(pikeStreetBuilder.copy {
        it.name = "Troy armor and shields"
    })

    mobService.buildShopkeeper(
        "the armorer",
        "the armorer is here",
        "tbd",
        Lasher(),
        armorShop,
        listOf(),
    )

    connect(connection)
        .toRoom(road1, Direction.NORTH)
        .toRoom(road2, Direction.NORTH)
        .toRoom(road3, Direction.NORTH)

    connect(road1)
        .toRoom(
            listOf(
                Pair(weaponShop, Direction.WEST),
                Pair(armorShop, Direction.EAST),
            )
        )

    createMudSchool(mobService, roomService, itemService, road2)
}
