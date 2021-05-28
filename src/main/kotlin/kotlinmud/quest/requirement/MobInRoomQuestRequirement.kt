package kotlinmud.quest.requirement

import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.MobIdentifier
import kotlinmud.quest.type.QuestRequirement
import kotlinmud.quest.type.QuestRequirementType

class MobInRoomQuestRequirement(private val mobService: MobService, val identifier: MobIdentifier) : QuestRequirement {
    override val questRequirementType = QuestRequirementType.MOB_IN_ROOM

    override fun doesSatisfy(mob: PlayerMob): Boolean {
        val mobs = mobService.findMobsInRoom(mob.room)
        return mobs.find { it.identifier == identifier } != null
    }
}
