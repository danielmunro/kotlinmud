package kotlinmud.mob.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.quest.type.QuestRequirement
import kotlinmud.mob.quest.type.QuestRequirementType

class MobInRoomQuestRequirement(private val mob: MobDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.MOB_IN_ROOM

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.room == this.mob.room
    }
}