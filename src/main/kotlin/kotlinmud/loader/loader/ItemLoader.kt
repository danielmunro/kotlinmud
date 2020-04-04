package kotlinmud.loader.loader

import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.AttributesBuilder
import kotlinmud.item.Drink
import kotlinmud.item.Food
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.item.itemBuilder
import kotlinmud.loader.Tokenizer

class ItemLoader(private val tokenizer: Tokenizer) : WithAttrLoader() {
    var id = 0
    var name = ""
    var description = ""
    var value = 0
    var drink = Drink.NONE
    var food = Food.NONE
    var quantity = 0
    override var props: Map<String, String> = mapOf()

    override fun load(): Any {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        props = tokenizer.parseProperties()
        drink = Drink.valueOf(strAttr("drink", "none").toUpperCase())
        food = Food.valueOf(strAttr("food", "none").toUpperCase())
        quantity = intAttr("quantity", 0)
        parseAttributes()
        val builder = itemBuilder(id, name)
        val affects = parseAffectTypes(tokenizer).map {
            AffectInstance(it, -1)
        }

        return builder
            .description(description)
            .worth(props["value"]?.toInt() ?: 1)
            .level(props["level"]?.toInt() ?: 1)
            .weight(props["weight"]?.toDouble() ?: 1.0)
            .drink(drink)
            .food(food)
            .quantity(quantity)
            .material(Material.valueOf(strAttr("material", "organic").toUpperCase()))
            .position(Position.valueOf(strAttr("position", "none").toUpperCase()))
            .affects(affects.toMutableList())
            .attributes(
                AttributesBuilder()
                    .hp(hp)
                    .mana(mana)
                    .mv(mv)
                    .hit(hit)
                    .dam(dam)
                    .strength(str)
                    .intelligence(int)
                    .wisdom(wis)
                    .dexterity(dex)
                    .constitution(con)
                    .acSlash(acSlash)
                    .acBash(acBash)
                    .acPierce(acPierce)
                    .acMagic(acMagic)
                    .build()
            )
    }
}
