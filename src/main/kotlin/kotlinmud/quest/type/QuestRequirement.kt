package kotlinmud.quest.type

import kotlinmud.mob.model.Mob

interface QuestRequirement {
    val questRequirementType: QuestRequirementType

    fun doesSatisfy(mob: Mob): Boolean
}
