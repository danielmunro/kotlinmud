package kotlinmud.startup.model

data class MobRespawnModel(
    val mobId: Int,
    val maxAmountInRoom: Int,
    val maxAmountInGame: Int,
    val roomId: Int,
)
