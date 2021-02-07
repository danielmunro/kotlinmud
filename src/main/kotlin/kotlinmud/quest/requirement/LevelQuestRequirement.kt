package kotlinmud.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class LevelQuestRequirement(val level: Int) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.LEVEL

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.level >= level
    }
}
