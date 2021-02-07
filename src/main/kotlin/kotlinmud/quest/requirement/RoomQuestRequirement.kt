package kotlinmud.quest.requirement

import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.room.dao.RoomDAO

class RoomQuestRequirement(private val room: RoomDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ROOM

    override fun doesSatisfy(mob: MobDAO): Boolean {
        return mob.room == room
    }
}
