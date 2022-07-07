package kotlinmud.persistence.model.builder

import kotlinmud.persistence.model.ItemModel
import kotlinmud.persistence.model.Model
import kotlinmud.room.model.Area

class ItemModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()
    override var respawns = listOf<RespawnSpec>()
    var area: Area? = null

    override fun build(): Model {
        return ItemModel(
            id,
            name,
            brief,
            description,
            area!!,
            keywords,
            respawns,
        )
    }
}
