package kotlinmud.quest.service

import kotlinmud.item.model.Item
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.factory.createItemQuestRequirement
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createRoomQuestRequirement
import kotlinmud.quest.helper.questList
import kotlinmud.quest.model.Quest
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.QuestReward
import kotlinmud.room.service.RoomService
import kotlinmud.type.RoomCanonicalId

class QuestBuilderService(
    private val mobService: MobService,
    private val roomService: RoomService,
) {
    lateinit var type: QuestType
    var level = 1
    var name = ""
    var description = ""
    var brief = ""
    val acceptConditions = mutableListOf<QuestRequirement>()
    val submitConditions = mutableListOf<QuestRequirement>()
    val rewards = mutableListOf<QuestReward>()

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
        return Quest(
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
