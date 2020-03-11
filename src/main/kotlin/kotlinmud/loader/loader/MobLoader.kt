package kotlinmud.loader.loader

import kotlinmud.attributes.Attributes
import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.MobModel
import kotlinmud.mob.Disposition
import kotlinmud.mob.JobType
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.createRaceFromString

class MobLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var brief = ""
    var description = ""
    var disposition = ""
    var attributes: Map<String, String> = mapOf()

    override fun load(): MobModel {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        brief = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = tokenizer.parseString()
        attributes = tokenizer.parseProperties()
        val hp = intAttr("hp")
        val mana = intAttr("mana")
        val mv = intAttr("mv")
        val job = strAttr("job", "none")
        val specialization = strAttr("specialization", "none")

        return MobModel(
            id,
            name,
            brief,
            description,
            Disposition.valueOf(disposition.toUpperCase()),
            hp,
            mana,
            mv,
            intAttr("level"),
            createRaceFromString(strAttr("race", "human")),
            SpecializationType.valueOf(specialization.toUpperCase()),
            Attributes(hp, mana, mv),
            JobType.valueOf(job.toUpperCase())
        )
    }

    private fun intAttr(name: String): Int {
        return attributes[name]?.toInt() ?: 0
    }

    private fun strAttr(name: String, default: String = ""): String {
        return attributes[name] ?: default
    }
}
