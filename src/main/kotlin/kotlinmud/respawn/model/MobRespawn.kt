package kotlinmud.respawn.model

import kotlinmud.mob.builder.MobBuilder
import kotlinmud.respawn.type.Respawn

class MobRespawn(
    val mobBuilder: MobBuilder,
    val area: String,
    val maxAmount: Int = 1,
) : Respawn
