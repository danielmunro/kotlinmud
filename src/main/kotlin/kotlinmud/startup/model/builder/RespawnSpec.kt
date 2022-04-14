package kotlinmud.startup.model.builder

data class RespawnSpec(
    val type: RespawnType,
    val maxAmount: Int,
    val maxInGame: Int,
    val targetId: Int,
)
