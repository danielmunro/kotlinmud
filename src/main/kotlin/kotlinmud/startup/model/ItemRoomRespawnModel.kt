package kotlinmud.startup.model

import kotlinmud.room.type.Area

data class ItemRoomRespawnModel(
    val area: Area,
    val itemId: Int,
    val maxAmountInRoom: Int,
    val maxAmountInGame: Int,
    val roomId: Int,
)
