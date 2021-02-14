package kotlinmud.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.repository.findQuestByMobCardAndType
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType

class PriorQuestRequirement(private val priorQuest: QuestType) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.PRIOR_QUEST

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return findQuestByMobCardAndType(mob.mobCard!!, priorQuest)?.let {
            it.status == QuestStatus.SUBMITTED
        } ?: false
    }
}
