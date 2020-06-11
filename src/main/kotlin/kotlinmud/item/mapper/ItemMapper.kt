package kotlinmud.item.mapper

import kotlinmud.affect.mapper.mapAffects
import kotlinmud.attributes.mapper.mapAttributes
import kotlinmud.fs.helper.int
import kotlinmud.fs.helper.str
import kotlinmud.fs.saver.mapper.optional
import kotlinmud.item.model.Item
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.DamageType

fun mapItem(item: Item): String {
    return """${int(item.id)}
${str(item.name)}
${str(item.description)}
${str(item.type.toString())}
${int(item.worth)}
${int(item.level)}
${str(item.material.toString())}
${str(item.weight.toString())}
${mapAttributes(item.attributes)}
${str(generateProps(item))}
${mapAffects(item.affects)}
"""
}

fun generateProps(item: Item): String {
    val attr = item.attributes
    val optional = optional(
        item.position != Position.NONE,
        "position: ${item.position.value}"
    ) +
            optional(item.drink != Drink.NONE, "drink: ${item.drink}") +
            optional(item.food != Food.NONE, "food: ${item.food}") +
            optional(item.quantity > 0, "quantity: ${item.quantity}") +
            optional(item.attackVerb != "", "attackVerb: ${item.attackVerb}") +
            optional(item.damageType != DamageType.NONE, "damageType: ${item.damageType}") +
            optional(item.decayTimer > 0, "decayTimer: ${item.decayTimer}") +
            optional(item.hasInventory, "hasInventory: true") +
            optional(!item.canOwn, "canOwn: false") +
            optional(
                attr.hasAC(),
                "ac: ${attr.acBash}-${attr.acSlash}-${attr.acPierce}-${attr.acMagic}"
            )
    return if (optional != "") optional.substring(0, optional.length - 2) else ""
}
