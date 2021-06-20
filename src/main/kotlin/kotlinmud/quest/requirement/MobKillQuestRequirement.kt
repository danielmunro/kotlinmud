package kotlinmud.quest.requirement

import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.quest.type.QuestType

class MobKillQuestRequirement(private val questType: QuestType, private val amount: Int) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.MOB_KILLED

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        return false
    }
}
