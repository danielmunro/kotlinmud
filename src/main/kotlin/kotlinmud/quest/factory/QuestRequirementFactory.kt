package kotlinmud.quest.factory

import kotlinmud.mob.repository.findMobByCanonicalId
import kotlinmud.quest.requirement.MobInRoomQuestRequirement
import kotlinmud.quest.requirement.RoomQuestRequirement
import kotlinmud.room.repository.findRoomByCanonicalId
import kotlinmud.type.CanonicalId

fun createMobInRoomQuestRequirement(id: CanonicalId): MobInRoomQuestRequirement {
    return MobInRoomQuestRequirement(findMobByCanonicalId(id))
}

fun createRoomQuestRequirement(id: CanonicalId): RoomQuestRequirement {
    return RoomQuestRequirement(findRoomByCanonicalId(id))
}
