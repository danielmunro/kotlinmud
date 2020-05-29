package kotlinmud.player.loader

import kotlinmud.attributes.Attributes
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.AttributesLoader
import kotlinmud.mob.model.Appetite
import kotlinmud.player.model.MobCard
import kotlinmud.player.model.MobCardBuilder

class MobCardLoader(private val tokenizer: Tokenizer) {
    companion object {
        fun intVal(value: String?): Int {
            return value?.toInt() ?: 0
        }
    }

    fun load(): MobCard {
        val builder = MobCardBuilder()
            .playerEmail(tokenizer.parseString())
            .mobName(tokenizer.parseString())
            .experience(tokenizer.parseInt())
            .experiencePerLevel(tokenizer.parseInt())
            .sacPoints(tokenizer.parseInt())
            .trains(tokenizer.parseInt())
            .practices(tokenizer.parseInt())
            .appetite(
                Appetite(
                    tokenizer.parseInt(),
                    tokenizer.parseInt(),
                    tokenizer.parseInt(),
                    tokenizer.parseInt()
                )
            )
            .bounty(tokenizer.parseInt())
            .skillPoints(tokenizer.parseInt())
        val trainedAttributes = mutableListOf<Attributes>()
        val attributesLoader = AttributesLoader(tokenizer)
        while (tokenizer.peek() != "end") {
            trainedAttributes.add(attributesLoader.load())
        }
        builder.trainedAttributes(trainedAttributes)
        return builder.build()
    }
}
