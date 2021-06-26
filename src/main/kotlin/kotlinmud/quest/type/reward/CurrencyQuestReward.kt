package kotlinmud.quest.type.reward

import kotlinmud.mob.type.CurrencyType
import kotlinmud.quest.type.QuestRewardType

class CurrencyQuestReward(
    val currencyType: CurrencyType,
    override val amount: Int,
) : QuestReward {
    override val rewardType = QuestRewardType.Currency
}
