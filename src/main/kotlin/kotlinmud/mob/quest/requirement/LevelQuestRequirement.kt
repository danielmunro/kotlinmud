package kotlinmud.mob.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.quest.type.QuestRequirement
import kotlinmud.mob.quest.type.QuestRequirementType

class LevelQuestRequirement(val level: Int) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.LEVEL

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.level >= level
    }
}