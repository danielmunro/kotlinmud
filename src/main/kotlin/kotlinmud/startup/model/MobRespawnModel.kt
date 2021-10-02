package kotlinmud.startup.model

import kotlinmud.room.type.Area

data class MobRespawnModel(
    val area: Area,
    val mobId: Int,
    val maxAmountInRoom: Int,
    val maxAmountInGame: Int,
    val roomId: Int,
)
