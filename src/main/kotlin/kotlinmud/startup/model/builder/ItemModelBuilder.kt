package kotlinmud.startup.model.builder

import kotlinmud.room.type.Area
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.Model

class ItemModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()
    override var respawns = listOf<RespawnSpec>()
    var area = Area.None

    override fun build(): Model {
        return ItemModel(
            id,
            name,
            brief,
            description,
            area,
            keywords,
            respawns,
        )
    }
}
