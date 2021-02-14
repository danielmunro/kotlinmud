package kotlinmud.quest.type.reward

import kotlinmud.faction.type.FactionType
import kotlinmud.quest.type.QuestRewardType

class FactionScoreQuestReward(
    val factionType: FactionType,
    val score: Int
) : QuestReward {
    override val rewardType: QuestRewardType
        get() = QuestRewardType.FactionScore
}
