package kotlinmud.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class PriorQuestRequirement : QuestRequirement {
    override val questRequirementType = QuestRequirementType.PRIOR_QUEST

    override fun doesSatisfy(mob: MobDAO): Boolean {
        TODO("Not yet implemented")
    }
}