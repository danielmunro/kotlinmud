package kotlinmud.startup.model.builder

import kotlinmud.room.type.Area
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.Model

class MobModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()
    override var respawns = listOf<RespawnSpec>()
    var area = Area.None

    override fun build(): Model {
        return MobModel(
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
