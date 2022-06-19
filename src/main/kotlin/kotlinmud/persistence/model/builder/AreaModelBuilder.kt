package kotlinmud.persistence.model.builder

import kotlinmud.persistence.model.AreaModel
import kotlinmud.persistence.model.Model

class AreaModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()
    override var respawns = listOf<RespawnSpec>()

    override fun build(): Model {
        return AreaModel(
            id,
            name,
        )
    }
}
