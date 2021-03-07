package kotlinmud.quest.type

import kotlinmud.mob.model.PlayerMob

interface QuestRequirement {
    val questRequirementType: QuestRequirementType

    fun doesSatisfy(mob: PlayerMob): Boolean
}
