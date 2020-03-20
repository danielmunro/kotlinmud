package kotlinmud.loader.loader

import kotlinmud.attributes.Attribute
import kotlinmud.item.Drink
import kotlinmud.item.Item
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.Model

class ItemLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var value = 0
    var hit = 0
    var dam = 0
    var acSlash = 0
    var acBash = 0
    var acPierce = 0
    var acMagic = 0
    var drink = Drink.NONE
    var quantity = 0
    override var props: Map<String, String> = mapOf()

    override fun load(): Model {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        props = tokenizer.parseProperties()
        val ac = strAttr("ac", "0-0-0-0").split("-")
        hit = intAttr("hit")
        dam = intAttr("dam")
        drink = Drink.valueOf(strAttr("drink", "none").toUpperCase())
        quantity = intAttr("quantity", 0)
        acSlash = ac[0].toInt()
        acBash = ac[1].toInt()
        acPierce = ac[2].toInt()
        acMagic = ac[3].toInt()

        return Item.Builder(id, name)
            .setDescription(description)
            .setValue(props["value"]?.toInt() ?: 1)
            .setLevel(props["level"]?.toInt() ?: 1)
            .setWeight(props["weight"]?.toDouble() ?: 1.0)
            .setDrink(drink)
            .setQuantity(quantity)
            .setAttribute(Attribute.HIT, hit)
            .setAttribute(Attribute.DAM, dam)
            .setAttribute(Attribute.AC_SLASH, acSlash)
            .setAttribute(Attribute.AC_PIERCE, acPierce)
            .setAttribute(Attribute.AC_BASH, acBash)
            .setAttribute(Attribute.AC_MAGIC, acMagic)
            .setMaterial(Material.valueOf(strAttr("material", "organic").toUpperCase()))
            .setPosition(Position.valueOf(strAttr("position", "none").toUpperCase()))
    }
}
