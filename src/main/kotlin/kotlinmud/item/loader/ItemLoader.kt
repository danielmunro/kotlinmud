package kotlinmud.item.loader

import kotlinmud.affect.loader.AffectLoader
import kotlinmud.attributes.loader.AttributesLoader
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.loader.intAttr
import kotlinmud.fs.loader.area.loader.strAttr
import kotlinmud.fs.service.CURRENT_LOAD_SCHEMA_VERSION
import kotlinmud.item.factory.itemBuilder
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.DamageType

class ItemLoader(
    private val tokenizer: Tokenizer,
    private val loadSchemaVersion: Int = CURRENT_LOAD_SCHEMA_VERSION
) : Loader {
    override fun load(): ItemBuilder {
        val id = tokenizer.parseInt()
        val name = tokenizer.parseString()
        val description = tokenizer.parseString()
        val type = ItemType.valueOf(tokenizer.parseString())
        val worth = tokenizer.parseInt()
        val level = tokenizer.parseInt()
        val material = Material.valueOf(tokenizer.parseString())
        val weight = tokenizer.parseString().toDouble()
        val attributesLoader = AttributesLoader(tokenizer)
        val attributes = attributesLoader.load()
        val props = tokenizer.parseProperties()
        val drink = Drink.valueOf(strAttr(props["drink"], "none").toUpperCase())
        val food = Food.valueOf(strAttr(props["food"], "none").toUpperCase())
        val attackVerb = strAttr(props["attackVerb"], "")
        val damageType = DamageType.valueOf(strAttr(props["damageType"], "none").toUpperCase())
        val quantity = intAttr(props["quantity"], 0)
        val builder = itemBuilder(id, name)
        val affects = AffectLoader(tokenizer).load()

        return builder
            .description(description)
            .worth(worth)
            .level(level)
            .weight(weight)
            .drink(drink)
            .food(food)
            .type(type)
            .quantity(quantity)
            .material(material)
            .position(Position.valueOf(strAttr(props["position"], "none").toUpperCase()))
            .affects(affects.toMutableList())
            .attackVerb(attackVerb)
            .damageType(damageType)
            .attributes(attributes)
            .decayTimer(props["decayTimer"]?.toInt() ?: -1)
            .hasInventory(props["hasInventory"]?.toBoolean() ?: false)
            .canOwn(props["canOwn"]?.toBoolean() ?: true)
    }
}
