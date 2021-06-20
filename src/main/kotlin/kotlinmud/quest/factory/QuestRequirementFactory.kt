package kotlinmud.quest.factory

import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.requirement.LevelQuestRequirement
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.requirement.MobKillQuestRequirement
import kotlinmud.quest.requirement.PriorQuestRequirement
import kotlinmud.quest.requirement.RoomQuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.room.model.Room

fun createLevelQuestRequirement(level: Int): LevelQuestRequirement {
    return LevelQuestRequirement(level)
}

fun createPriorQuestRequirement(questType: QuestType): PriorQuestRequirement {
    return PriorQuestRequirement(questType)
}

fun createMobInRoomQuestRequirement(mobService: MobService, id: QuestGiver): MobInRoomQuestRequirement {
    return MobInRoomQuestRequirement(mobService, id)
}

fun createRoomQuestRequirement(room: Room): RoomQuestRequirement {
    return RoomQuestRequirement(room)
}

fun createMobKillQuestRequirement(questType: QuestType, amount: Int): MobKillQuestRequirement {
    return MobKillQuestRequirement(questType, amount)
}
