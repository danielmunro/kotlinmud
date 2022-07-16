package kotlinmud.persistence.model

import kotlinmud.room.model.Area

data class RoomModel(
    override var id: Int,
    var name: String,
    var description: String,
    val keywords: MutableMap<String, String>,
    var area: Area,
) : Model
