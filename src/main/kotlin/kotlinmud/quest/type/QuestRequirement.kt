package kotlinmud.quest.type

import kotlinmud.mob.model.PlayerMob

interface QuestRequirement {
    val questRequirementType: QuestRequirementType
    val amount: Int

    fun doesSatisfy(mob: PlayerMob): Boolean
}
