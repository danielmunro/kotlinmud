package kotlinmud.world.itrias.lorimir

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.ItemMobRespawn
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.race.impl.Dwarf
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.respawn.helper.respawn
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId

fun createLorimirForestOutpost(mobService: MobService, itemService: ItemService, roomService: RoomService): Room {
    val builder = RoomBuilder(roomService).also { it.area = Area.LorimirForestOutpost }

    val room1 = build(
        builder.also {
            it.name = "Around a fire pit"
            it.description = """A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.
    
    A sign flickers against the light of the fire.""".trimMargin()
            it.canonicalId = RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
        }
    )

    ItemBuilder(itemService)
        .name("a cobblestone fire-pit")
        .description("a fire emanates from the circular pit.")
        .canOwn(false)
        .material(Material.STONE)
        .type(ItemType.FURNITURE)
        .room(room1)
        .build()

    ItemBuilder(itemService)
        .name("a large wooden sign on a post")
        .description(
"""The sign reads:

+-------------------------------------------------+
|                                                 |
|        Type `quest list` in order to see        |
|        an available quest.                      |
|                                                 |
+-------------------------------------------------+"""
        )
        .canOwn(false)
        .material(Material.WOOD)
        .type(ItemType.FURNITURE)
        .room(room1)
        .build()

    val room2 = build(
        builder.also {
            it.name = "Inside a lean-to shelter"
            it.description = "bar"
            it.canonicalId = RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
        }
    )

    val room3 = build(
        builder.also {
            it.name = "A blacksmith shack"
        }
    )
    val room4 = build(
        builder.also {
            it.name = "A trail near the camp"
        }
    )
    val room6 = build(
        builder.also {
            it.name = "A makeshift mess hall"
        }
    )
    val room5 = build(
        builder.also {
            it.name = "Outside the camp"
        }
    )

    connect(room1)
        .to(
            listOf(
                Pair(room2, Direction.NORTH),
                Pair(room3, Direction.WEST),
                Pair(room4, Direction.EAST),
                Pair(room5, Direction.SOUTH),
            )
        )
    connect(room5).to(room6, Direction.EAST)

    MobBuilder(mobService).also {
        it.name = "Blacksmith Felig"
        it.brief = "a blacksmith stands over a forge, monitoring his work"
        it.description = "a large giant is here, forging a weapon"
        it.room = room3
        it.job = JobType.SHOPKEEPER
        it.race = Giant()
    }.build()

    MobBuilder(mobService).also {
        it.name = "Barbosa the cook"
        it.brief = "a messy and overworked cook wipes away his brow sweat"
        it.description = "tbd"
        it.room = room6
        it.job = JobType.SHOPKEEPER
        it.race = Dwarf()
        it.canonicalId = MobCanonicalId.Barbosa
    }.build()

    respawn(
        ItemMobRespawn(
            ItemCanonicalId.Bread,
            ItemBuilder(itemService).also {
                it.type = ItemType.FOOD
                it.worth = 10
                it.name = "a small hard loaf of bread"
                it.description = "foo"
                it.material = Material.ORGANIC
                it.food = Food.BREAD
                it.canonicalId = ItemCanonicalId.Bread
            },
            MobCanonicalId.Barbosa,
            100,
        )
    )

    MobBuilder(mobService).also {
        it.name = "Recruiter Esmer"
        it.brief = "a cloaked figure sits against a log, facing the fire, reading a leaflet"
        it.description = "Recruiter Esmer is here"
        it.room = room2
        it.job = JobType.QUEST
        it.race = Human()
        it.canonicalId = MobCanonicalId.PraetorianRecruiterEsmer
    }.build()

    return room4
}
