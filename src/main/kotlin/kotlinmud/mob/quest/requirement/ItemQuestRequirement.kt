package kotlinmud.mob.quest.requirement

import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.quest.type.QuestRequirement
import kotlinmud.mob.quest.type.QuestRequirementType

class ItemQuestRequirement(private val item: ItemDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ITEM

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.items.contains(item)
    }
}