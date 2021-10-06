package kotlinmud.startup.model

import kotlinmud.room.type.Area

data class ItemMobRespawnModel(
    val area: Area,
    val itemId: Int,
    val maxAmountForMob: Int,
    val maxAmountInGame: Int,
    val mobId: Int,
)
