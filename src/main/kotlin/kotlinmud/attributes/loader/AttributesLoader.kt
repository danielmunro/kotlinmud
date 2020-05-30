package kotlinmud.attributes.loader

import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.attributes.model.Attributes
import kotlinmud.fs.loader.Tokenizer

class AttributesLoader(private val tokenizer: Tokenizer) {
    fun load(): Attributes {
        val stats = parse()
        val vitals = parse()
        val hitdam = parse()
        val ac = parse()
        return AttributesBuilder()
            .strength(stats[0])
            .intelligence(stats[1])
            .wisdom(stats[2])
            .dexterity(stats[3])
            .constitution(stats[4])
            .hp(vitals[0])
            .mana(vitals[1])
            .mv(vitals[2])
            .hit(hitdam[0])
            .dam(hitdam[1])
            .acSlash(ac[0])
            .acBash(ac[1])
            .acPierce(ac[2])
            .acMagic(ac[3])
            .build()
    }

    private fun parse(): List<Int> {
        return tokenizer.parseString().split(" ").map { it.toInt() }
    }
}
