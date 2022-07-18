package kotlinmud.room.model

import kotlinmud.room.type.Lighting

data class Area(
    var id: Int,
    val name: String,
    val lighting: Lighting,
)
