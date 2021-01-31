package kotlinmud.mob.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.quest.type.QuestRequirement
import kotlinmud.mob.quest.type.QuestRequirementType

class PriorQuestRequirement : QuestRequirement {
    override val questRequirementType = QuestRequirementType.PRIOR_QUEST

    override fun doesSatisfy(mob: MobDAO): Boolean {
        TODO("Not yet implemented")
    }
}