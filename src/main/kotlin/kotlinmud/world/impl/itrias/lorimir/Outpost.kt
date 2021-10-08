package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.faction.type.FactionType
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService
import kotlinmud.world.type.AreaConnections

fun createLorimirForestOutpost(svc: AreaBuilderService): AreaConnections {
    svc.roomBuilder(
        "Around a fire pit",
        """A circular cobblestone fire-pit serves as the centerpiece for the modest outpost that surrounds you.
    
    A sign flickers against the light of the fire.""",
    )

    svc.buildRoom("fire pit") {
        it.canonicalId = RoomCanonicalId.FindRecruiterPraetorianGuard
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
+-------------------------------------------------+""",
    ).also {
        it.canOwn = false
        it.material = Material.WOOD
        it.type = ItemType.FURNITURE
    }.build()

    svc.buildRoom("shelter") {
        it.name = "Inside a lean-to shelter"
        it.description = "bar"
        it.canonicalId = RoomCanonicalId.PraetorianGuardRecruiterFound
    }

    svc.connectRooms("fire pit", "shelter", Direction.NORTH)

    svc.buildQuestGiver(
        "Recruiter Esmer",
        "a cloaked figure sits against a log, facing the fire, reading a leaflet",
        "Recruiter Esmer is here",
        Human(),
        QuestGiver.RecruiterEsmer,
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
        mapOf(
            Pair(
                svc.itemBuilder(
                    "a crude iron sword",
                    "tbd",
                    4.0,
                    100,
                ).makeWeapon(
                    Weapon.SWORD,
                    DamageType.SLASH,
                    "twack",
                    Material.IRON,
                    1,
                    2,
                ),
                10,
            ),
            Pair(
                svc.itemBuilder(
                    "a crude iron cudgel",
                    "tbd",
                    8.0,
                    100,
                ).makeWeapon(
                    Weapon.MACE,
                    DamageType.POUND,
                    "smash",
                    Material.IRON,
                    1,
                    2,
                ),
                10,
            ),
            Pair(
                svc.itemBuilder(
                    "a crude iron dagger",
                    "tbd",
                    1.0,
                    100,
                ).makeWeapon(
                    Weapon.DAGGER,
                    DamageType.PIERCE,
                    "stab",
                    Material.IRON,
                    1,
                    2,
                ),
                10,
            ),
        ),
    )

    svc.buildRoom("trail") {
        it.name = "A trail near the camp"
    }
        .buildRoom("to outpost", Direction.EAST)

    svc.connectRooms("fire pit", "trail", Direction.EAST)

    svc.buildRoom("campfire") {
        it.name = "By the campfire"
    }

    svc.connectRooms("fire pit", "campfire", Direction.SOUTH)

    svc.buildRoom("mess hall") {
        it.name = "A makeshift mess hall"
        it.canonicalId = RoomCanonicalId.BarbosaCook
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
                    5,
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
                    0.1,
                    8,
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

    svc.questBuilder(
        QuestType.BarbosaSupplies,
        "collect mushrooms for Barbosa",
        "mushroom collecting",
        "Supplies have been short, aye. Can ye' help a poor cook out, 'n pad me supplies?",
    ).also {
        it.addRoomAcceptQuestRequirement(RoomCanonicalId.BarbosaCook)
        it.addItemCountSubmitQuestRequirement("tasty forest mushrooms", { item -> item.food == Food.MUSHROOM }, 6)
        it.addRoomSubmitQuestRequirement(RoomCanonicalId.BarbosaCook)
        it.rewards.addAll(
            listOf(
                CurrencyQuestReward(CurrencyType.Silver, 8),
                FactionScoreQuestReward(FactionType.PraetorianGuard, 50),
                ExperienceQuestReward(350),
            )
        )
    }.build()

    svc.questBuilder(
        QuestType.FindPraetorianGuardRecruiter,
        "find a recruiter for the Praetorian Guard",
        "tbd",
        "tbd",
    ).also {
        it.addRoomAcceptQuestRequirement(RoomCanonicalId.FindRecruiterPraetorianGuard)
        it.addMobInRoomSubmitCondition("Recruiter Esmer", QuestGiver.RecruiterEsmer)
        it.rewards.addAll(
            listOf(
                FactionScoreQuestReward(FactionType.PraetorianGuard, 100),
                ExperienceQuestReward(1000),
                CurrencyQuestReward(CurrencyType.Gold, 1),
                CurrencyQuestReward(CurrencyType.Silver, 15),
            )
        )
    }.build()

    return mapOf(
        Pair(Area.LorimirForest, svc.getRoomFromLabel("to outpost")),
        Pair(Area.TroyOutskirts, svc.getRoomFromLabel("to outpost")),
    )
}
