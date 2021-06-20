package kotlinmud.quest.impl.pyreforgeFarm

import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.CurrencyType
import kotlinmud.quest.factory.createMobKillQuestRequirement
import kotlinmud.quest.type.Quest
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.CurrencyQuestReward
import kotlinmud.quest.type.reward.ExperienceQuestReward

class ClearPyreforgeRodents(mobService: MobService) : Quest {
    override val type = QuestType.CLEAR_PYREFORGE_RODENTS
    override val level = 1
    override val name = "Help Beatrice Pyreforge clear her rodent infestation"
    override val brief = "tbd"
    override val description = "yolo"
    override val acceptConditions = listOf<QuestRequirement>()
    override val submitConditions = listOf(
        createMobKillQuestRequirement(QuestType.CLEAR_PYREFORGE_RODENTS, 6),
    )
    override val rewards = listOf(
        ExperienceQuestReward(200),
        CurrencyQuestReward(CurrencyType.Silver, 45),
        CurrencyQuestReward(CurrencyType.Copper, 50),
    )
}
