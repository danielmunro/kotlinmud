package kotlinmud.world.impl.itrias.troy

import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Material
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
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
): Room {
    val pikeStreetBuilder = roomService.builder(
        "Pike Street",
        "tbd",
        Area.Troy,
    )

    val road1 = build(pikeStreetBuilder)
    val road2 = build(pikeStreetBuilder)
    val road3 = build(pikeStreetBuilder)
    val gate = build(
        pikeStreetBuilder.copy {
            it.name = "Troy North Gate"
        }
    )

    val weaponShop = build(
        pikeStreetBuilder.copy {
            it.name = "Troy weapon smith shop"
        }
    )

    mobService.buildShopkeeper(
        "the weaponsmith",
        "the weaponsmith is here",
        "tbd",
        Kender(),
        weaponShop,
        listOf(
            Pair(
                itemService.buildWeapon(
                    "an iron sword",
                    "tbd",
                    4.0,
                    Weapon.SWORD,
                    DamageType.SLASH,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
            Pair(
                itemService.buildWeapon(
                    "an iron axe",
                    "tbd",
                    4.0,
                    Weapon.AXE,
                    DamageType.SLASH,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
            Pair(
                itemService.buildWeapon(
                    "an iron mace",
                    "tbd",
                    7.0,
                    Weapon.MACE,
                    DamageType.POUND,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
            Pair(
                itemService.buildWeapon(
                    "an iron dagger",
                    "tbd",
                    2.0,
                    Weapon.DAGGER,
                    DamageType.PIERCE,
                    Material.IRON,
                    1,
                    2,
                    10,
                ),
                100
            ),
        ),
    )

    val armorShop = build(
        pikeStreetBuilder.copy {
            it.name = "Troy armor and shields"
        }
    )

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
        .toRoom(gate, Direction.NORTH)

    connect(road1)
        .toRoom(
            listOf(
                Pair(weaponShop, Direction.WEST),
                Pair(armorShop, Direction.EAST),
            )
        )

    createMudSchool(mobService, roomService, itemService, road2)

    return gate
}
