package kotlinmud.loader.loader

import kotlinmud.attributes.Attribute
import kotlinmud.item.Drink
import kotlinmud.item.Food
import kotlinmud.item.Item
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.Model

class ItemLoader(private val tokenizer: Tokenizer) : WithAttrLoader() {
    var id = 0
    var name = ""
    var description = ""
    var value = 0
    var drink = Drink.NONE
    var food = Food.NONE
    var quantity = 0
    override var props: Map<String, String> = mapOf()

    override fun load(): Model {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        props = tokenizer.parseProperties()
        drink = Drink.valueOf(strAttr("drink", "none").toUpperCase())
        food = Food.valueOf(strAttr("food", "none").toUpperCase())
        quantity = intAttr("quantity", 0)
        parseAttributes()

        return Item.Builder(id, name)
            .setDescription(description)
            .setValue(props["value"]?.toInt() ?: 1)
            .setLevel(props["level"]?.toInt() ?: 1)
            .setWeight(props["weight"]?.toDouble() ?: 1.0)
            .setDrink(drink)
            .setFood(food)
            .setQuantity(quantity)
            .setAttribute(Attribute.HP, hp)
            .setAttribute(Attribute.MANA, mana)
            .setAttribute(Attribute.MV, mv)
            .setAttribute(Attribute.HIT, hit)
            .setAttribute(Attribute.DAM, dam)
            .setAttribute(Attribute.STR, str)
            .setAttribute(Attribute.INT, int)
            .setAttribute(Attribute.WIS, wis)
            .setAttribute(Attribute.DEX, dex)
            .setAttribute(Attribute.CON, con)
            .setAttribute(Attribute.AC_SLASH, acSlash)
            .setAttribute(Attribute.AC_PIERCE, acPierce)
            .setAttribute(Attribute.AC_BASH, acBash)
            .setAttribute(Attribute.AC_MAGIC, acMagic)
            .setMaterial(Material.valueOf(strAttr("material", "organic").toUpperCase()))
            .setPosition(Position.valueOf(strAttr("position", "none").toUpperCase()))
    }
}
