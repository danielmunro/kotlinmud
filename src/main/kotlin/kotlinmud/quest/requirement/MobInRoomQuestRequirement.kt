package kotlinmud.quest.requirement

import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class MobInRoomQuestRequirement(private val mobService: MobService, val canonicalId: MobCanonicalId) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.MOB_IN_ROOM

    override fun doesSatisfy(mob: Mob): Boolean {
        val mobs = mobService.findMobsInRoom(mob.room)
        return mobs.find { it.canonicalId == canonicalId } != null
    }
}
