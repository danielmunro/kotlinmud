package kotlinmud.quest.type.reward

import kotlinmud.item.dao.ItemDAO
import kotlinmud.quest.type.QuestRewardType

class ItemQuestReward(val createItem: () -> ItemDAO) : QuestReward {
    override val rewardType: QuestRewardType
        get() = QuestRewardType.Item

    fun createItem(): ItemDAO {
        return createItem()
    }
}