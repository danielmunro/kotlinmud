package kotlinmud.attributes.mapper

import kotlinmud.attributes.model.Attributes

fun mapAttributes(attributes: Attributes): String {
    return """${attributes.strength} ${attributes.intelligence} ${attributes.wisdom} ${attributes.dexterity} ${attributes.constitution}~
${attributes.hp} ${attributes.mana} ${attributes.mv}~
${attributes.hit} ${attributes.dam}~
${attributes.acSlash} ${attributes.acBash} ${attributes.acPierce} ${attributes.acMagic}~
"""
}
