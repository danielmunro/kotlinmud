package kotlinmud.quest.type

import kotlinmud.helper.Identifiable
import kotlinmud.quest.type.reward.QuestReward

interface Quest : Identifiable {
    val type: QuestType
    val level: Int
    override val name: String
    val description: String
    val acceptConditions: List<QuestRequirement>
    val submitConditions: List<QuestRequirement>
    val rewards: List<QuestReward>
}
