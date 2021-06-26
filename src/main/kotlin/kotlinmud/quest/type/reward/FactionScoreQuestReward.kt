package kotlinmud.quest.type.reward

import kotlinmud.faction.type.FactionType
import kotlinmud.quest.type.QuestRewardType

class FactionScoreQuestReward(
    val factionType: FactionType,
    override val amount: Int,
) : QuestReward {
    override val rewardType = QuestRewardType.FactionScore
}
