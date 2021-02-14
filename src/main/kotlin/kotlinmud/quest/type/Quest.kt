package kotlinmud.quest.type

import kotlinmud.helper.Identifiable
import kotlinmud.io.service.RequestService
import kotlinmud.quest.type.reward.QuestReward

interface Quest : Identifiable {
    val type: QuestType
    override val name: String
    val description: String
    val acceptConditions: List<QuestRequirement>
    val submitConditions: List<QuestRequirement>
    val rewards: List<QuestReward>
}
