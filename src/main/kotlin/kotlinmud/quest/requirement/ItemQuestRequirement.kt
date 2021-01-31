package kotlinmud.quest.requirement

import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class ItemQuestRequirement(private val item: ItemDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ITEM

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.items.contains(item)
    }
}