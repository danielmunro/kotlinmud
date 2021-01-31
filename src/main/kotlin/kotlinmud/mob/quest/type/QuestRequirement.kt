package kotlinmud.mob.quest.type

import kotlinmud.mob.dao.MobDAO

interface QuestRequirement {
    val questRequirementType: QuestRequirementType

    fun doesSatisfy(mob: MobDAO): Boolean
}