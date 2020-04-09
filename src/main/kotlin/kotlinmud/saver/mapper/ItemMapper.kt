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
            optional(attr.hp > 0, "hp: ${attr.hp}") +
            optional(attr.mana > 0, "mana: ${attr.mana}") +
            optional(attr.mv > 0, "mv: ${attr.mv}") +
            optional(attr.hit > 0, "hit: ${attr.hit}") +
            optional(attr.dam > 0, "dam: ${attr.dam}") +
            optional(attr.strength > 0, "str: ${attr.strength}") +
            optional(attr.intelligence > 0, "int: ${attr.intelligence}") +
            optional(attr.wisdom > 0, "wis: ${attr.wisdom}") +
            optional(attr.dexterity > 0, "dex: ${attr.dexterity}") +
            optional(attr.constitution > 0, "con: ${attr.constitution}") +
            optional(attr.hasAC(), "ac: ${attr.acBash}-${attr.acSlash}-${attr.acPierce}-${attr.acMagic}")
}
