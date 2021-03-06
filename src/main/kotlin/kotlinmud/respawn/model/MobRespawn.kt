package kotlinmud.respawn.model

import kotlinmud.mob.builder.MobBuilder
import kotlinmud.respawn.type.Respawn
import kotlinmud.room.type.Area

class MobRespawn(
    val mobBuilder: MobBuilder,
    val area: Area,
    val maxAmount: Int = 1,
) : Respawn
