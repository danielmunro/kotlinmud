package kotlinmud.persistence.model

data class MobRespawnModel(
    val area: String,
    val mobId: Int,
    val maxAmountInRoom: Int,
    val maxAmountInGame: Int,
    val roomId: Int,
)
