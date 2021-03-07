package kotlinmud.mob.model

import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.type.Area

class MobRespawn(
    val canonicalId: MobCanonicalId,
    val mobBuilder: MobBuilder,
    val area: Area,
    val maxAmount: Int = 1,
)
