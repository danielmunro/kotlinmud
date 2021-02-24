package kotlinmud.quest.requirement

import kotlinmud.mob.model.Mob
import kotlinmud.quest.repository.findQuestByMobCardAndType
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import org.jetbrains.exposed.sql.transactions.transaction

class PriorQuestRequirement(private val priorQuest: QuestType) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.PRIOR_QUEST

    override fun doesSatisfy(mob: Mob): Boolean {
        return findQuestByMobCardAndType(transaction { mob.mobCard!! }, priorQuest)?.let {
            it.status == QuestStatus.SUBMITTED
        } ?: false
    }
}
