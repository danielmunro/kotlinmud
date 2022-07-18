package kotlinmud.persistence.model

import kotlinmud.persistence.model.builder.RespawnSpec
import kotlinmud.room.model.Area

data class MobModel(
    override var id: Int,
    var name: String,
    var brief: String,
    var description: String,
    var area: Area,
    var keywords: Map<String, String>,
    var respawns: List<RespawnSpec>,
) : Model
