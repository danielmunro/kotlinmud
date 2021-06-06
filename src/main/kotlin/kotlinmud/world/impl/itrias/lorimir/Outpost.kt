package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.QuestGiver
import kotlinmud.room.helper.connect
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService

fun createLorimirForestOutpost(areaBuilderService: AreaBuilderService): Room {
    areaBuilderService.roomBuilder(
        "Around a fire pit",
        """A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.
    
    A sign flickers against the light of the fire.""",
    )

    val room1 = areaBuilderService.buildRoomCopy {
        it.canonicalId = RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
    }

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

    val room2 = areaBuilderService.buildRoomCopy {
        it.name = "Inside a lean-to shelter"
        it.description = "bar"
        it.canonicalId = RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
    }

    areaBuilderService.buildQuestGiver(
        "Recruiter Esmer",
        "a cloaked figure sits against a log, facing the fire, reading a leaflet",
        "Recruiter Esmer is here",
        Human(),
        QuestGiver.PraetorianRecruiterEsmer,
    )

    val room3 = areaBuilderService.buildRoomCopy {
        it.name = "A blacksmith shack"
    }

    areaBuilderService.buildShopkeeper(
        "Blacksmith Felig",
        "a blacksmith stands over a forge, monitoring his work",
        "tbd",
        Giant(),
        mapOf(),
    )

    val room4 = areaBuilderService.buildRoomCopy {
        it.name = "A trail near the camp"
    }

    val room5 = areaBuilderService.buildRoomCopy {
        it.name = "By the campfire"
    }

    val room6 = areaBuilderService.buildRoomCopy {
        it.name = "A makeshift mess hall"
    }

    areaBuilderService.buildShopkeeper(
        "Barbosa the cook",
        "a messy and overworked cook wipes away his brow sweat",
        "a large cook stops moving long enough to wipe sweat from his eyebrow.",
        Human(),
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
            ),
            Pair(
                areaBuilderService.itemBuilder(
                    "preserved meat",
                    "foo",
                ).also { item ->
                    item.type = ItemType.FOOD
                    item.worth = 65
                    item.material = Material.ORGANIC
                    item.food = Food.PRESERVED_MEAT
                },
                100,
            )
        )
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

    return room4
}
