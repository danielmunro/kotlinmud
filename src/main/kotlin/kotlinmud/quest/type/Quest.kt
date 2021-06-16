package kotlinmud.quest.type

import kotlinmud.helper.Noun
import kotlinmud.quest.type.reward.QuestReward

interface Quest : Noun {
    val type: QuestType
    val level: Int
    override val name: String
    override val brief: String
    override val description: String
    val acceptConditions: List<QuestRequirement>
    val submitConditions: List<QuestRequirement>
    val rewards: List<QuestReward>
}
