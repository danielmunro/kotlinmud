package kotlinmud.player.loader

import kotlinmud.attributes.AttributesBuilder
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.mob.Appetite
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
        val attr = tokenizer.parseProperties()
        builder.attributes(
            AttributesBuilder()
                .strength(intVal(attr["str"]))
                .intelligence(intVal(attr["int"]))
                .wisdom(intVal(attr["wis"]))
                .dexterity(intVal(attr["dex"]))
                .constitution(intVal(attr["con"]))
                .hp(intVal(attr["hp"]))
                .mana(intVal(attr["mana"]))
                .mv(intVal(attr["mv"]))
                .hit(intVal(attr["hit"]))
                .dam(intVal(attr["dam"]))
                .acSlash(intVal(attr["acSlash"]))
                .acBash(intVal(attr["acBash"]))
                .acPierce(intVal(attr["acPierce"]))
                .acMagic(intVal(attr["acMagic"]))
                .build()
        )
        return builder.build()
    }
}
