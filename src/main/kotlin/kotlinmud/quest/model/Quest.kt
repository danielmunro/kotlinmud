package kotlinmud.quest.model

import kotlinmud.helper.Noun
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestType
import kotlinmud.quest.type.reward.QuestReward

class Quest(
    val id: Int,
    val type: QuestType,
    val level: Int,
    override val name: String,
    override val brief: String,
    override val description: String,
    val acceptConditions: List<QuestRequirement>,
    val submitConditions: List<QuestRequirement>,
    val rewards: List<QuestReward>,
) : Noun
