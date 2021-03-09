package kotlinmud.world.itrias.lorimir

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId

fun createLorimirForestOutpost(mobService: MobService, itemService: ItemService, roomService: RoomService): Room {
    val builder = RoomBuilder(roomService).area(Area.LorimirForestOutpost)

    val room1 = builder.name("Around a fire pit")
        .description(
"""A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.

A sign flickers against the light of the fire.""".trimMargin()
        )
        .canonicalId(RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD)
        .build()

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

    val room2 = builder.name("Inside a lean-to shelter")
        .description("bar")
        .canonicalId(RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND)
        .build()

    val room3 = builder.name("A blacksmith shack").build()
    val room4 = builder.name("A trail near the camp").build()
    val room5 = builder.name("Outside the camp").build()

    connect(room1)
        .to(
            listOf(
                Pair(room2, Direction.NORTH),
                Pair(room3, Direction.WEST),
                Pair(room4, Direction.EAST),
                Pair(room5, Direction.SOUTH),
            )
        )

    MobBuilder(mobService).also {
        it.name = "Blacksmith Felig"
        it.brief = "a blacksmith stands over a forge, monitoring his work"
        it.description = "a large giant is here, forging a weapon"
        it.room = room3
        it.job = JobType.SHOPKEEPER
        it.race = Giant()
    }.build()

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
