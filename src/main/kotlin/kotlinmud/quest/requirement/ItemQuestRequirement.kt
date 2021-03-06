package kotlinmud.quest.requirement

import kotlinmud.item.model.Item
import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class ItemQuestRequirement(
    private val predicate: (Item) -> Boolean,
    val itemName: String,
    override val amount: Int,
) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ITEM

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        return mob.items.count(predicate) >= amount
    }
}
