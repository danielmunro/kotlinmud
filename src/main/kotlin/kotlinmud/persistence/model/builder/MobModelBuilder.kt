package kotlinmud.persistence.model.builder

import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.model.Model
import kotlinmud.room.model.Area

class MobModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()
    override var respawns = listOf<RespawnSpec>()
    var area: Area? = null

    override fun build(): Model {
        return MobModel(
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
