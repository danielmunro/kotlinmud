package kotlinmud.persistence.model

data class ItemRoomRespawnModel(
    val area: String,
    val itemId: Int,
    val maxAmountInRoom: Int,
    val maxAmountInGame: Int,
    val roomId: Int,
)
