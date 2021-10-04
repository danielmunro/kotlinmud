package kotlinmud.startup.model.builder

import kotlinmud.startup.model.AreaModel
import kotlinmud.startup.model.Model

class AreaModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()

    override fun build(): Model {
        return AreaModel(
            id,
            name,
        )
    }
}
