package kotlinmud.fs.saver.mapper

import kotlinmud.attributes.Attributes

fun mapAttributes(attributes: Attributes): String {
    return """${attributes.strength} ${attributes.intelligence} ${attributes.wisdom} ${attributes.dexterity} ${attributes.constitution}~
${attributes.hp} ${attributes.mana} ${attributes.mv}~
${attributes.hit} ${attributes.dam}~
${attributes.acSlash} ${attributes.acBash} ${attributes.acPierce} ${attributes.acMagic}~
"""
}
