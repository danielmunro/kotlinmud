package kotlinmud.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import org.jetbrains.exposed.sql.transactions.transaction

class MobInRoomQuestRequirement(val mob: MobDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.MOB_IN_ROOM

    override fun doesSatisfy(mob: MobDAO): Boolean {
        val checkMob = this.mob
        return transaction { mob.room == checkMob.room }
    }
}
