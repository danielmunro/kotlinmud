package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.QuestGiver
import kotlinmud.room.builder.build
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService

fun createLorimirForestOutpost(areaBuilderService: AreaBuilderService): Room {
    val builder = areaBuilderService.roomBuilder(
        "Around a fire pit",
        """A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.
    
    A sign flickers against the light of the fire.""",
    )

    areaBuilderService.copyRoomBuilder {
        it.canonicalId = RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
    }

    val room1 = areaBuilderService.buildRoom()

    areaBuilderService.itemBuilder(
        "a cobblestone fire-pit",
        "a fire emanates from the circular pit.",
    ).also {
        it.canOwn = false
        it.material = Material.STONE
        it.type = ItemType.FURNITURE
        it.room = room1
    }.build()

    areaBuilderService.itemBuilder(
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

    areaBuilderService.buildShopkeeper(
        "Blacksmith Felig",
        "a blacksmith stands over a forge, monitoring his work",
        "a large giant is here, forging a weapon",
        Giant(),
        room3,
        mapOf(),
    )

    areaBuilderService.buildShopkeeper(
        "Barbosa the cook",
        "a messy and overworked cook wipes away his brow sweat",
        "a large cook stops moving long enough to wipe sweat from his eyebrow.",
        Human(),
        room6,
        mapOf(
            Pair(
                areaBuilderService.itemBuilder(
                    "a small hard loaf of bread",
                    "foo",
                    0.1,
                ).also { item ->
                    item.type = ItemType.FOOD
                    item.worth = 10
                    item.material = Material.ORGANIC
                    item.food = Food.BREAD
                },
                100,
            )
        )
    )

    areaBuilderService.buildQuestGiver(
        "Recruiter Esmer",
        "a cloaked figure sits against a log, facing the fire, reading a leaflet",
        "Recruiter Esmer is here",
        Human(),
        room2,
        QuestGiver.PraetorianRecruiterEsmer,
    )

    return room4
}
