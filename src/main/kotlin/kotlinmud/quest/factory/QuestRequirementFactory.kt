package kotlinmud.quest.factory

import kotlinmud.mob.repository.findMobByCanonicalId
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.type.CanonicalId

fun createMobInRoomQuestRequirement(id: CanonicalId): MobInRoomQuestRequirement {
    return MobInRoomQuestRequirement(findMobByCanonicalId(id))
}
