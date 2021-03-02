package kotlinmud.quest.requirement

import kotlinmud.mob.model.Mob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.room.model.Room

class RoomQuestRequirement(private val room: Room) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ROOM

    override fun doesSatisfy(mob: Mob): Boolean {
        return mob.room == room
    }
}
