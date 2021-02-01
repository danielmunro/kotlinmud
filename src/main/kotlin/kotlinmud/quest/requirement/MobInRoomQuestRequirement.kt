package kotlinmud.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class MobInRoomQuestRequirement(private val mob: MobDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.MOB_IN_ROOM

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.room == this.mob.room
    }
}
