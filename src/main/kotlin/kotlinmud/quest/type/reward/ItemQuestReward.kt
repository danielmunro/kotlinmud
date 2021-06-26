package kotlinmud.quest.type.reward

import kotlinmud.item.model.Item
import kotlinmud.quest.type.QuestRewardType

class ItemQuestReward(val createItemCallable: () -> Item, override val amount: Int = 1) : QuestReward {
    override val rewardType = QuestRewardType.Item

    fun createItems(): List<Item> {
        return List(amount) { createItemCallable() }
    }
}
