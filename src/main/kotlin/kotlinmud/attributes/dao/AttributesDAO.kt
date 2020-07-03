package kotlinmud.attributes.dao

import kotlinmud.attributes.table.Attributes
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class AttributesDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AttributesDAO>(Attributes)

    var hp by Attributes.hp
    var mana by Attributes.mana
    var mv by Attributes.mv
    var strength by Attributes.strength
    var intelligence by Attributes.intelligence
    var wisdom by Attributes.wisdom
    var dexterity by Attributes.dexterity
    var constitution by Attributes.constitution
    var hit by Attributes.hit
    var dam by Attributes.dam
    var acBash by Attributes.acBash
    var acSlash by Attributes.acSlash
    var acPierce by Attributes.acPierce
    var acMagic by Attributes.acMagic
}
