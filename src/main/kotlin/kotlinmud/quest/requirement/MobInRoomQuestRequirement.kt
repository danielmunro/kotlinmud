package kotlinmud.quest.requirement

import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class MobInRoomQuestRequirement(
    private val mobService: MobService,
    val mobName: String,
    private val predicate: (Mob) -> Boolean
) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.MOB_IN_ROOM
    override val amount = 0

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        val mobs = mobService.findMobsInRoom(mob.room)
        return mobs.find(predicate) != null
    }
}
