package kotlinmud.quest.type.reward

import kotlinmud.quest.type.QuestRewardType

class ExperienceQuestReward(override val amount: Int) : QuestReward {
    override val rewardType = QuestRewardType.Experience
}
