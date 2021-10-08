package kotlinmud.startup.model.builder

import kotlinmud.startup.model.Model
import kotlinmud.startup.model.QuestModel

class QuestModelBuilder : Builder {
    override var id = 0
    override var name = ""
    override var brief = ""
    override var description = ""
    override var keywords = mapOf<String, String>()

    override fun build(): Model {
        return QuestModel(
            id,
            name,
            brief,
            description,
            keywords,
        )
    }
}
