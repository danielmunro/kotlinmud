package kotlinmud.persistence.model.builder

import kotlinmud.persistence.model.DoorModel
import kotlinmud.persistence.model.Model

class DoorModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()
    override var respawns = listOf<RespawnSpec>()

    override fun build(): Model {
        return DoorModel(
            id,
            name,
            brief,
            description,
            keywords,
        )
    }
}
