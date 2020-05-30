package kotlinmud.item.mapper

import kotlinmud.attributes.mapper.mapAttributes
import kotlinmud.fs.int
import kotlinmud.fs.saver.mapper.optional
import kotlinmud.fs.str
import kotlinmud.item.model.Item
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.DamageType

fun mapItem(item: Item): String {
    return """${int(item.id)}
${str(item.name)}
${str(item.description)}
${mapAttributes(item.attributes)}
${str(generateProps(item))}
${str(getAffects(item))}
"""
}

fun getAffects(item: Item): String {
    return item.affects.joinToString { it.affectType.value }
}

fun generateProps(item: Item): String {
    val attr = item.attributes
    val required = "material: ${item.material.value}, weight: ${item.weight}, value: ${item.worth}, level: ${item.level}"
    val optional = optional(
        item.position != Position.NONE,
        "position: ${item.position.value}"
    ) +
            optional(item.drink != Drink.NONE, "drink: ${item.drink}") +
            optional(item.food != Food.NONE, "food: ${item.food}") +
            optional(item.quantity > 0, "quantity: ${item.quantity}") +
            optional(item.attackVerb != "", "attackVerb: ${item.attackVerb}") +
            optional(item.damageType != DamageType.NONE, "damageType: ${item.damageType}") +
            optional(
                attr.hasAC(),
                "ac: ${attr.acBash}-${attr.acSlash}-${attr.acPierce}-${attr.acMagic}"
            )
    return required + if (optional != "") ", ${optional.substring(0, optional.length - 2)}" else ""
}
