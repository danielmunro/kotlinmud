package kotlinmud.quest.impl.pyreforgeFarm

import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.quest.factory.createMobInRoomQuestRequirement
import kotlinmud.quest.factory.createMobKillQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward

class ClearPyreforgeRodents(mobService: MobService) : Quest {
    override val type = QuestType.CLEAR_PYREFORGE_RODENTS
    override val level = 1
    override val name = "help Beatrice Pyreforge clear her rodent infestation"
    override val brief = "tbd"
    override val description = "yolo"
    override val acceptConditions = listOf<QuestRequirement>(
        createMobInRoomQuestRequirement(mobService, QuestGiver.BeatricePyreforge),
    )
    override val submitConditions = listOf(
        createMobInRoomQuestRequirement(mobService, QuestGiver.BeatricePyreforge),
        createMobKillQuestRequirement(QuestType.CLEAR_PYREFORGE_RODENTS, 6),
    )
    override val rewards = listOf(
        ExperienceQuestReward(200),
        CurrencyQuestReward(CurrencyType.Silver, 45),
        CurrencyQuestReward(CurrencyType.Copper, 50),
    )
}
