package kotlinmud.quest.service

import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.QuestGiver
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

    fun addMobInRoomAcceptCondition(questGiver: QuestGiver) {
        acceptConditions.add(createMobInRoomQuestRequirement(mobService, questGiver))
    }

    fun addMobInRoomSubmitCondition(questGiver: QuestGiver) {
        submitConditions.add(createMobInRoomQuestRequirement(mobService, questGiver))
    }

    fun addRoomAcceptQuestRequirement(roomCanonicalId: RoomCanonicalId) {
        acceptConditions.add(
            createRoomQuestRequirement(roomService.findOne { it.canonicalId == roomCanonicalId }!!)
        )
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
