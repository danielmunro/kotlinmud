package kotlinmud.saver.mapper

import kotlinmud.item.Drink
import kotlinmud.item.Food
import kotlinmud.item.Item
import kotlinmud.item.Position

fun mapItem(item: Item): String {
    return """#${item.id}
${item.name}~
${item.description}~
${generateProps(item)}~
${getAffects(item)}~
"""
}

fun getAffects(item: Item): String {
    return item.affects.joinToString { it.affectType.value }
}

fun generateProps(item: Item): String {
    val attr = item.attributes
    return "material: ${item.material.value}, weight: ${item.weight}, value: ${item.worth}, level: ${item.level}, " +
            optional(item.position != Position.NONE, "position: ${item.position.value}") +
            optional(item.drink != Drink.NONE, "drink: ${item.drink}") +
            optional(item.food != Food.NONE, "food: ${item.food}") +
            optional(item.quantity > 0, "quantity: ${item.quantity}") +
            optional(attr.hit > 0, "hit: ${attr.hit}") +
            optional(attr.dam > 0, "dam: ${attr.dam}") +
            optional(attr.hasAC(), "ac: ${attr.acBash}-${attr.acSlash}-${attr.acPierce}-${attr.acMagic}")
}

fun optional(condition: Boolean, output: String): String {
    return if (condition) "$output, " else ""
}
