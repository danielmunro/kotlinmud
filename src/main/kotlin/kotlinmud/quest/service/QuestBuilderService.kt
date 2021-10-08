package kotlinmud.quest.service

import kotlinmud.faction.type.FactionType
import kotlinmud.helper.logger
import kotlinmud.item.model.Item
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.factory.createItemQuestRequirement
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createRoomQuestRequirement
import kotlinmud.quest.helper.questList
import kotlinmud.quest.model.Quest
import kotlinmud.quest.requirement.ItemQuestRequirement
import kotlinmud.quest.requirement.RoomQuestRequirement
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward
import kotlinmud.quest.type.reward.FactionScoreQuestReward
import kotlinmud.quest.type.reward.QuestReward
import kotlinmud.room.service.RoomService
import kotlinmud.type.RoomCanonicalId

class QuestBuilderService(
    private val mobService: MobService,
    private val roomService: RoomService,
) {
    var id = 0
    lateinit var type: QuestType
    var level = 1
    var name = ""
    var description = ""
    var brief = ""
    var itemId = 0
    var itemCount = 0
    var faction: FactionType? = null
    var score = 0
    val acceptConditions = mutableListOf<QuestRequirement>()
    val submitConditions = mutableListOf<QuestRequirement>()
    val rewards = mutableListOf<QuestReward>()
    val logger = logger(this)

    fun setFromKeyword(keyword: String, value: String) {
        when (keyword) {
            "type" -> {
                type = QuestType.valueOf(value)
            }
            "level" -> {
                level = value.toInt()
            }
            "experience" -> {
                rewards.add(ExperienceQuestReward(value.toInt()))
            }
            "worth" -> {
                rewards.add(CurrencyQuestReward(CurrencyType.Silver, value.toInt()))
            }
            "room_accept" -> {
                val roomAcceptId = value.toInt()
                acceptConditions.add(
                    RoomQuestRequirement(
                        roomService.findOne { it.id == roomAcceptId }!!
                    )
                )
            }
            "room_submit" -> {
                val roomSubmitId = value.toInt()
                submitConditions.add(
                    RoomQuestRequirement(
                        roomService.findOne { it.id == roomSubmitId }!!
                    )
                )
            }
            "item_id" -> {
                itemId = value.toInt()
            }
            "item_count" -> {
                itemCount = value.toInt()
            }
            "faction" -> {
                faction = FactionType.valueOf(value)
            }
            "score" -> {
                score = value.toInt()
            }
            else -> {
                logger.warn("keyword in quest service not implemented -- {}", keyword)
            }
        }
    }

    fun addMobInRoomAcceptCondition(mobName: String, questGiver: QuestGiver) {
        acceptConditions.add(
            createMobInRoomQuestRequirement(mobService, mobName) { mob -> mob.questGiver == questGiver }
        )
    }

    fun addMobInRoomSubmitCondition(mobName: String, questGiver: QuestGiver) {
        submitConditions.add(createMobInRoomQuestRequirement(mobService, mobName) { mob -> mob.questGiver == questGiver })
    }

    fun addRoomAcceptQuestRequirement(roomCanonicalId: RoomCanonicalId) {
        acceptConditions.add(
            createRoomQuestRequirement(roomService.findOne { it.canonicalId == roomCanonicalId }!!)
        )
    }

    fun addRoomSubmitQuestRequirement(roomCanonicalId: RoomCanonicalId) {
        submitConditions.add(
            createRoomQuestRequirement(roomService.findOne { it.canonicalId == roomCanonicalId }!!)
        )
    }

    fun addItemCountSubmitQuestRequirement(itemName: String, predicate: (Item) -> Boolean, count: Int = 1) {
        submitConditions.add(createItemQuestRequirement(itemName, predicate, count))
    }

    fun build(): Quest {
        faction?.let {
            if (score > 0) {
                rewards.add(
                    FactionScoreQuestReward(it, score)
                )
            }
        }

        if (itemCount > 0) {
            submitConditions.add(
                ItemQuestRequirement(
                    { it.id == itemId },
                    "TBD",
                    itemCount,
                )
            )
        }

        return Quest(
            id,
            type,
            level,
            name,
            description,
            brief,
            acceptConditions,
            submitConditions,
            rewards,
        ).also {
            questList.add(it)
        }
    }
}
