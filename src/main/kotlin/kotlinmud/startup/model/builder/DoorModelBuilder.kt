package kotlinmud.startup.model.builder

import kotlinmud.startup.model.DoorModel
import kotlinmud.startup.model.Model

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
