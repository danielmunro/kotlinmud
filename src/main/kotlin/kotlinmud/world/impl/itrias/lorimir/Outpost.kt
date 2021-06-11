package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.QuestGiver
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService

fun createLorimirForestOutpost(svc: AreaBuilderService): Room {
    svc.roomBuilder(
        "Around a fire pit",
        """A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.
    
    A sign flickers against the light of the fire.""",
    )

    svc.buildRoom("fire pit") {
        it.canonicalId = RoomCanonicalId.FIND_RECRUITER_PRAETORIAN_GUARD
    }

    svc.roomItemBuilder(
        "a cobblestone fire-pit",
        "a fire emanates from the circular pit.",
    ).also {
        it.canOwn = false
        it.material = Material.STONE
        it.type = ItemType.FURNITURE
    }.build()

    svc.roomItemBuilder(
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
    }.build()

    svc.buildRoom("shelter") {
        it.name = "Inside a lean-to shelter"
        it.description = "bar"
        it.canonicalId = RoomCanonicalId.PRAETORIAN_GUARD_RECRUITER_FOUND
    }

    svc.connectRooms("fire pit", "shelter", Direction.NORTH)

    svc.buildQuestGiver(
        "Recruiter Esmer",
        "a cloaked figure sits against a log, facing the fire, reading a leaflet",
        "Recruiter Esmer is here",
        Human(),
        QuestGiver.PraetorianRecruiterEsmer,
    )

    svc.buildRoom("blacksmith") {
        it.name = "A blacksmith shack"
    }

    svc.connectRooms("fire pit", "blacksmith", Direction.WEST)

    svc.buildShopkeeper(
        "Blacksmith Felig",
        "a blacksmith stands over a forge, monitoring his work",
        "tbd",
        Giant(),
        mapOf(),
    )

    val room4 = svc.buildRoom("trail") {
        it.name = "A trail near the camp"
    }.lastRoom

    svc.connectRooms("fire pit", "trail", Direction.EAST)

    svc.buildRoom("campfire") {
        it.name = "By the campfire"
    }

    svc.connectRooms("fire pit", "campfire", Direction.SOUTH)

    svc.buildRoom("mess hall") {
        it.name = "A makeshift mess hall"
    }

    svc.connectRooms("campfire", "mess hall", Direction.EAST)

    svc.buildShopkeeper(
        "Barbosa the cook",
        "a messy and overworked cook wipes away his brow sweat",
        "a large cook stops moving long enough to wipe sweat from his eyebrow.",
        Human(),
        mapOf(
            Pair(
                svc.itemBuilder(
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
                svc.itemBuilder(
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

    return room4
}
