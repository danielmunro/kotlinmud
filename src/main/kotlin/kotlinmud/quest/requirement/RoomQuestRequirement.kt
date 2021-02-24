package kotlinmud.quest.requirement

import kotlinmud.mob.model.Mob
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class RoomQuestRequirement(private val room: RoomDAO) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.ROOM

    override fun doesSatisfy(mob: Mob): Boolean {
        return transaction { mob.room } == room
    }
}
