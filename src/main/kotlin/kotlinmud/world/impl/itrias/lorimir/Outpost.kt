package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Dwarf
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobIdentifier
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.ItemMobRespawn
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId

fun createLorimirForestOutpost(mobService: MobService, itemService: ItemService, roomService: RoomService): Room {
    val builder = roomService.builder(
        "Around a fire pit",
        """A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.
    
    A sign flickers against the light of the fire.""",
        Area.LorimirForestOutpost,
    )

    val room1 = build(
        builder.copy {
            it.canonicalId = RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
        }
    )

    itemService.builder(
        "a cobblestone fire-pit",
        "a fire emanates from the circular pit."
    ).also {
        it.canOwn = false
        it.material = Material.STONE
        it.type = ItemType.FURNITURE
        it.room = room1
    }.build()

    itemService.builder(
        "a large wooden sign on a post",
        """The sign reads:

+-------------------------------------------------+
|                                                 |
|        Type `quest list` in order to see        |
|        an available quest.                      |
|                                                 |
+-------------------------------------------------+"""
    ).also {
        it.canOwn = false
        it.material = Material.WOOD
        it.type = ItemType.FURNITURE
        it.room = room1
    }.build()

    val room2 = build(
        builder.copy {
            it.name = "Inside a lean-to shelter"
            it.description = "bar"
            it.canonicalId = RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
        }
    )

    val room3 = build(
        builder.copy {
            it.name = "A blacksmith shack"
        }
    )
    val room4 = build(
        builder.copy {
            it.name = "A trail near the camp"
        }
    )
    val room6 = build(
        builder.copy {
            it.name = "A makeshift mess hall"
        }
    )
    val room5 = build(
        builder.copy {
            it.name = "Outside the camp"
        }
    )

    connect(room1)
        .toRoom(
            listOf(
                Pair(room2, Direction.NORTH),
                Pair(room3, Direction.WEST),
                Pair(room4, Direction.EAST),
                Pair(room5, Direction.SOUTH),
            )
        )
    connect(room5).toRoom(room6, Direction.EAST)

    mobService.builder(
        "Blacksmith Felig",
        "a blacksmith stands over a forge, monitoring his work",
        "a large giant is here, forging a weapon",
        Giant(),
    ).also {
        it.room = room3
        it.makeShopkeeper()
    }.build()

    mobService.builder(
        "Barbosa the cook",
        "a messy and overworked cook wipes away his brow sweat",
        "a large cook stops moving long enough to wipe sweat from his eyebrow.",
        Dwarf(),
    ).also {
        it.room = room6
        it.makeShopkeeper()
        respawn(
            ItemMobRespawn(
                itemService.builder(
                    "a small hard loaf of bread",
                    "foo",
                    0.1,
                ).also { item ->
                    item.type = ItemType.FOOD
                    item.worth = 10
                    item.material = Material.ORGANIC
                    item.food = Food.BREAD
                    item.canonicalId = ItemCanonicalId.Bread
                },
                it.canonicalId,
                100,
            )
        )
    }.build()

    mobService.builder(
        "Recruiter Esmer",
        "a cloaked figure sits against a log, facing the fire, reading a leaflet",
        "Recruiter Esmer is here",
    ).also {
        it.room = room2
        it.job = JobType.QUEST
        it.identifier = MobIdentifier.PraetorianRecruiterEsmer
    }.build()

    return room4
}
