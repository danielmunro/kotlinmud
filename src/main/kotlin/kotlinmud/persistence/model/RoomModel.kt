package kotlinmud.persistence.model

import kotlinmud.room.model.Area

data class RoomModel(
    override val id: Int,
    val name: String,
    val description: String,
    val keywords: MutableMap<String, String>,
    var area: Area,
) : Model
