package kotlinmud.startup.model

import kotlinmud.room.type.Area
import kotlinmud.startup.model.builder.RespawnSpec

data class ItemModel(
    override val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val area: Area,
    val keywords: Map<String, String>,
    val respawns: List<RespawnSpec>,
) : Model
