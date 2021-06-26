package kotlinmud.quest.requirement

import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType

class PriorQuestRequirement(
    private val priorQuest: QuestType,
) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.PRIOR_QUEST
    override val amount = 0

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        return mob.quests[priorQuest]?.let { it.status == QuestStatus.SUBMITTED } ?: false
    }
}
