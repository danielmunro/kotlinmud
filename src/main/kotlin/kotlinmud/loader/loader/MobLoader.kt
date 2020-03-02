package kotlinmud.loader.loader

import kotlinmud.attributes.Attributes
import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.MobModel
import kotlinmud.mob.Disposition
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.createRaceFromString

class MobLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var disposition = ""
    var attributes: Map<String, String> = mapOf()

    override fun load(): MobModel {
        id = tokenizer.parseId()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        disposition = tokenizer.parseString()
        attributes = tokenizer.parseProperties()
        val hp = intAttr("hp")
        val mana = intAttr("mana")
        val mv = intAttr("mv")

        return MobModel(
            id,
            name,
            description,
            Disposition.valueOf(disposition.toUpperCase()),
            hp,
            mana,
            mv,
            intAttr("level"),
            createRaceFromString(strAttr("race")),
            SpecializationType.valueOf(strAttr("specialization").toUpperCase()),
            Attributes(hp, mana, mv)
        )
    }

    private fun intAttr(name: String): Int {
        return attributes[name]?.toInt() ?: 0
    }

    private fun strAttr(name: String): String {
        return attributes[name] ?: ""
    }
}
