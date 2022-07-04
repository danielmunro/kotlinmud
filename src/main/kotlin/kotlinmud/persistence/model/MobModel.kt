package kotlinmud.persistence.model

import kotlinmud.persistence.model.builder.RespawnSpec

data class MobModel(
    override val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val area: String,
    val keywords: Map<String, String>,
    val respawns: List<RespawnSpec>,
) : Model
