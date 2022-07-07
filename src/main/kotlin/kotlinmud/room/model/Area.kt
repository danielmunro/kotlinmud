package kotlinmud.room.model

import kotlinmud.room.type.Lighting

data class Area(
    val id: Int,
    val name: String,
    val lighting: Lighting,
)
