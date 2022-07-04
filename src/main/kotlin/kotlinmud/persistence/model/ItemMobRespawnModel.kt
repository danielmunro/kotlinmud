package kotlinmud.persistence.model

data class ItemMobRespawnModel(
    val area: String,
    val itemId: Int,
    val maxAmountForMob: Int,
    val maxAmountInGame: Int,
    val mobId: Int,
)
