package kotlinmud.quest.requirement

import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.room.model.Room

class RoomQuestRequirement(
    val room: Room,
) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ROOM
    override val amount = 0

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        return mob.room == room
    }
}
