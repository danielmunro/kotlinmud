package kotlinmud.quest.requirement

import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class LevelQuestRequirement(val level: Int) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.LEVEL

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        return mob.level >= level
    }
}
