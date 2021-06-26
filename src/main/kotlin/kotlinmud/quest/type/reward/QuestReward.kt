package kotlinmud.quest.type.reward

import kotlinmud.quest.type.QuestRewardType

interface QuestReward {
    val rewardType: QuestRewardType
    val amount: Int
}
