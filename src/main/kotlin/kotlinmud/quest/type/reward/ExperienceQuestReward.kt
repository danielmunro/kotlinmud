package kotlinmud.quest.type.reward

import kotlinmud.quest.type.QuestRewardType

class ExperienceQuestReward(val amount: Int) : QuestReward {
    override val rewardType: QuestRewardType
        get() = QuestRewardType.Experience
}
