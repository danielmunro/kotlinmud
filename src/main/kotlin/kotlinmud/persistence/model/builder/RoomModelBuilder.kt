package kotlinmud.persistence.model.builder

import kotlinmud.persistence.model.Model
import kotlinmud.persistence.model.RoomModel
import kotlinmud.room.model.Area

class RoomModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()
    override var respawns = listOf<RespawnSpec>()
    var area: Area? = null

    override fun build(): Model {
        return RoomModel(
            id,
            name,
            description,
            keywords.toMutableMap(),
            area!!,
        )
    }
}
