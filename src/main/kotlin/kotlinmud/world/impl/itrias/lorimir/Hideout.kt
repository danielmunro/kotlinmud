package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Material
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.impl.Amphibian
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.CurrencyType
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction

fun createGrongokHideout(mobService: MobService, roomService: RoomService, itemService: ItemService, connector: Room) {
    val builder = roomService.builder(
        "entrance to a cave",
        "tbd",
        Area.GrongokHideout,
    )

    val room1 = build(builder)
    val room2 = build(
        builder.also {
            it.name = "deep in a cave"
        }
    )

    val room3 = build(builder)
    val room4 = build(builder)
    val matrix = SimpleMatrixService(builder).build(2, 2)
    val room5 = build(builder)

    mobService.buildFodder(
        "Grongok",
        "a wild looking ogre is here",
        "Grongok the wild ogre is here.",
        Ogre(),
        8,
        Area.GrongokHideout,
        1,
    ).also {
        it.equipped = listOf(
            itemService.builder(
                "a large stone cudgel",
                "tbd",
                20.0,
            ).also { eq ->
                eq.makeWeapon(
                    Weapon.MACE,
                    DamageType.POUND,
                    "pound",
                    Material.STONE,
                    3,
                    3,
                )
            }.build()
        )
        it.currencies = mutableMapOf(
            Pair(CurrencyType.Gold, 1),
        )
    }

    mobService.buildFodder(
        "a warty toad",
        "a warty toad is here, looking for water",
        "tbd",
        Amphibian(),
        3,
        Area.GrongokHideout,
        3,
    )

    connect(connector)
        .toRoom(room1)
        .toRoom(room2)
        .toRoom(
            listOf(
                Pair(room3, Direction.SOUTH),
                Pair(room4, Direction.EAST),
            )
        )
        .toRoom(matrix[0][0])

    connect(matrix[1][1]).toRoom(room5)
}
