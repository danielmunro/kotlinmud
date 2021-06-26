package kotlinmud.quest.factory

import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.quest.requirement.ItemQuestRequirement
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.requirement.MobKillQuestRequirement
import kotlinmud.quest.requirement.PriorQuestRequirement
import kotlinmud.quest.requirement.RoomQuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.room.model.Room

fun createPriorQuestRequirement(questType: QuestType): PriorQuestRequirement {
    return PriorQuestRequirement(questType)
}

fun createMobInRoomQuestRequirement(mobService: MobService, mobName: String, predicate: (Mob) -> Boolean): MobInRoomQuestRequirement {
    return MobInRoomQuestRequirement(mobService, mobName, predicate)
}

fun createRoomQuestRequirement(room: Room): RoomQuestRequirement {
    return RoomQuestRequirement(room)
}

fun createMobKillQuestRequirement(questType: QuestType, mobName: String, amount: Int): MobKillQuestRequirement {
    return MobKillQuestRequirement(questType, mobName, amount)
}

fun createItemQuestRequirement(itemName: String, predicate: (Item) -> Boolean, count: Int = 1): ItemQuestRequirement {
    return ItemQuestRequirement(predicate, itemName, count)
}
