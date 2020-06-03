package kotlinmud.item.loader

import kotlinmud.affect.model.AffectInstance
import kotlinmud.attributes.loader.AttributesLoader
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.loader.intAttr
import kotlinmud.fs.loader.area.loader.parseAffects
import kotlinmud.fs.loader.area.loader.strAttr
import kotlinmud.item.factory.itemBuilder
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.DamageType

class ItemLoader(private val tokenizer: Tokenizer, private val loadSchemaVersion: Int) : Loader {
    override fun load(): Any {
        val id = tokenizer.parseInt()
        val name = tokenizer.parseString()
        val description = tokenizer.parseString()
        val attributesLoader = AttributesLoader(tokenizer)
        val attributes = attributesLoader.load()
        val props = tokenizer.parseProperties()
        val drink = Drink.valueOf(strAttr(props["drink"], "none").toUpperCase())
        val food = Food.valueOf(strAttr(props["food"], "none").toUpperCase())
        val attackVerb = strAttr(props["attackVerb"], "")
        val damageType = DamageType.valueOf(strAttr(props["damageType"], "none").toUpperCase())
        val quantity = intAttr(props["quantity"], 0)
        val builder = itemBuilder(id, name)
        val affects = parseAffects(tokenizer).map {
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
            .material(Material.valueOf(strAttr(props["material"], "organic").toUpperCase()))
            .position(Position.valueOf(strAttr(props["position"], "none").toUpperCase()))
            .affects(affects.toMutableList())
            .attackVerb(attackVerb)
            .damageType(damageType)
            .attributes(attributes)
    }
}
