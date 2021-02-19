package kotlinmud.quest.factory

import kotlinmud.mob.repository.findMobByCanonicalId
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.quest.requirement.LevelQuestRequirement
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.requirement.PriorQuestRequirement
import kotlinmud.quest.requirement.RoomQuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.type.CanonicalId

fun createLevelQuestRequirement(level: Int): LevelQuestRequirement {
    return LevelQuestRequirement(level)
}

fun createPriorQuestRequirement(questType: QuestType): PriorQuestRequirement {
    return PriorQuestRequirement(questType)
}

fun createMobInRoomQuestRequirement(id: MobCanonicalId): MobInRoomQuestRequirement {
    return MobInRoomQuestRequirement(findMobByCanonicalId(id))
}

fun createRoomQuestRequirement(id: CanonicalId): RoomQuestRequirement {
    return RoomQuestRequirement(findRoomByCanonicalId(id))
}
