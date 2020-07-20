package kotlinmud.attributes.dao

import kotlinmud.attributes.table.Attributes
import kotlinmud.attributes.type.Attribute
import kotlinmud.player.dao.MobCardDAO
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

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
    var mobCard by MobCardDAO optionalReferencedOn Attributes.mobCardId

    fun getAttribute(attribute: Attribute): Int {
        return transaction {
            when (attribute) {
                Attribute.HP -> hp
                Attribute.MANA -> mana
                Attribute.MV -> mv
                Attribute.STR -> strength
                Attribute.INT -> intelligence
                Attribute.WIS -> wisdom
                Attribute.DEX -> dexterity
                Attribute.CON -> constitution
                Attribute.AC_BASH -> acBash
                Attribute.AC_PIERCE -> acPierce
                Attribute.AC_SLASH -> acSlash
                Attribute.AC_MAGIC -> acMagic
                Attribute.DAM -> dam
                Attribute.HIT -> hit
            }
        }
    }

    fun setAttribute(attribute: Attribute, value: Int) {
        transaction {
            when (attribute) {
                Attribute.HP -> hp = value
                Attribute.MANA -> mana = value
                Attribute.MV -> mv = value
                Attribute.STR -> strength = value
                Attribute.INT -> intelligence = value
                Attribute.WIS -> wisdom = value
                Attribute.DEX -> dexterity = value
                Attribute.CON -> constitution = value
                Attribute.HIT -> hit = value
                Attribute.DAM -> dam = value
                Attribute.AC_BASH -> acBash = value
                Attribute.AC_SLASH -> acSlash = value
                Attribute.AC_PIERCE -> acPierce = value
                Attribute.AC_MAGIC -> acMagic = value
            }
        }
    }
}
