package kotlinmud.mob.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.quest.type.QuestRequirement
import kotlinmud.mob.quest.type.QuestRequirementType
import kotlinmud.room.dao.RoomDAO

class RoomQuestRequirement(private val room: RoomDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ROOM

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.room == room
    }
}