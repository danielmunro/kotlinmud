package kotlinmud.persistence.model

import kotlinmud.persistence.model.builder.RespawnSpec
import kotlinmud.room.type.Area

data class ItemModel(
    override val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val area: Area,
    val keywords: Map<String, String>,
    val respawns: List<RespawnSpec>,
) : Model
