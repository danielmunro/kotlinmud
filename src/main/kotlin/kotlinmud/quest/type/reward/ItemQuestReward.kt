package kotlinmud.quest.type.reward

import kotlinmud.item.model.Item
import kotlinmud.quest.type.QuestRewardType

class ItemQuestReward(val createItem: () -> Item) : QuestReward {
    override val rewardType: QuestRewardType
        get() = QuestRewardType.Item

    fun createItem(): Item {
        return createItem()
    }
}
