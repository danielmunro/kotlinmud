package kotlinmud.world.impl.itrias.lorimir

import kotlinmud.faction.type.FactionType
import kotlinmud.generator.service.SimpleMatrixService
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.factory.createPriorQuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.ItemAreaRespawn
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinmud.type.RoomCanonicalId
import kotlinmud.world.service.AreaBuilderService
import java.util.UUID

fun createLorimirForest(
    areaBuilderService: AreaBuilderService,
    connection: Room
): AreaBuilderService {
    val builder = areaBuilderService.roomBuilder(
        "Deep in the heart of Lorimir Forest",
        "tbd",
    )

    val matrix = SimpleMatrixService(builder).build(5, 5)

    areaBuilderService
        .startWith(connection)
        .buildRoom(Direction.SOUTH)
        .buildRoom("intersection", Direction.SOUTH) {
            it.name = "An intersection in the woods"
        }
        .buildRoom("to lake", Direction.EAST) {
            it.name = "A clearing in the woods"
        }
        .startWith("intersection")
        .buildRoom("to south trail", Direction.SOUTH) {
            it.name = "A winding trail in the woods"
        }

    areaBuilderService
        .startWith("intersection")
        .buildRoom("after intersection", Direction.WEST) {
            it.name = "Deeper into the forest"
        }
        .buildRoom("near tree", Direction.WEST) {
            it.name = "Around a massive tree"
        }
        .buildRoom(Direction.SOUTH)
        .buildRoom(Direction.WEST)
        .buildRoom(Direction.NORTH)
        .buildRoom(Direction.NORTH) {
            it.name = "A dark forest"
            it.description = "Deep in the heart of Lorimir Forest."
            it.canonicalId = RoomCanonicalId.PraetorianCaptainFound
        }
        .connectTo(matrix[0][0], Direction.DOWN)

    val uuid = UUID.randomUUID()
    respawn(
        ItemAreaRespawn(
            uuid,
            areaBuilderService.itemBuilder(
                "a small brown mushroom",
                "tbd",
                0.1,
                1,
            ).also {
                it.material = Material.ORGANIC
                it.food = Food.MUSHROOM
                it.canonicalId = uuid
                it.type = ItemType.FOOD
            },
            Area.LorimirForest,
            10,
        ),
    )

    areaBuilderService.buildQuestGiver(
        "Captain Bartok",
        "an imposing figure stands here. Her armor bears the emblem of the Praetorian Guard",
        "Captain Bartok is here",
        Human(),
        QuestGiver.PraetorianCaptainBartok,
    )

    areaBuilderService.buildFodder(
        "a small fox",
        "a small fox darts through the underbrush",
        "a small fox is here.",
        Canid(),
        3,
        10,
    )

    createGrongokHideout(areaBuilderService, matrix[0][4])

    areaBuilderService.questBuilder(
        QuestType.JOIN_PRAETORIAN_GUARD,
        "talk to Captain Bartok of the Praetorian Guard",
        "tbd",
        "tbd",
    ).also {
        it.acceptConditions.add(createPriorQuestRequirement(QuestType.FIND_PRAETORIAN_GUARD_RECRUITER))
        it.addMobInRoomAcceptCondition(QuestGiver.RecruiterEsmer)
        it.addMobInRoomSubmitCondition(QuestGiver.PraetorianCaptainBartok)
        it.rewards.addAll(
            listOf(
                FactionScoreQuestReward(FactionType.PraetorianGuard, 100),
                ExperienceQuestReward(1000),
                CurrencyQuestReward(CurrencyType.Gold, 1),
                CurrencyQuestReward(CurrencyType.Silver, 15),
                CurrencyQuestReward(CurrencyType.Copper, 50),
            )
        )
    }.build()

    return areaBuilderService
}
