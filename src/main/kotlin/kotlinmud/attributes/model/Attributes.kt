package kotlinmud.attributes.model

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import kotlinmud.attributes.type.Attribute

const val startingHp = 20
const val startingMana = 100
const val startingMv = 100

@Builder
data class Attributes(
    @DefaultValue("0") val hp: Int = 0,
    @DefaultValue("0") val mana: Int = 0,
    @DefaultValue("0") val mv: Int = 0,
    @DefaultValue("0") val strength: Int = 0,
    @DefaultValue("0") val intelligence: Int = 0,
    @DefaultValue("0") val wisdom: Int = 0,
    @DefaultValue("0") val dexterity: Int = 0,
    @DefaultValue("0") val constitution: Int = 0,
    @DefaultValue("0") val hit: Int = 0,
    @DefaultValue("0") val dam: Int = 0,
    @DefaultValue("0") val acBash: Int = 0,
    @DefaultValue("0") val acSlash: Int = 0,
    @DefaultValue("0") val acPierce: Int = 0,
    @DefaultValue("0") val acMagic: Int = 0
) {
    fun getAttribute(attribute: Attribute): Int {
        return when (attribute) {
            Attribute.HP -> hp
            Attribute.MANA -> mana
            Attribute.MV -> mv
            Attribute.STR -> strength
            Attribute.INT -> intelligence
            Attribute.WIS -> wisdom
            Attribute.DEX -> dexterity
            Attribute.CON -> constitution
            Attribute.HIT -> hit
            Attribute.DAM -> dam
            Attribute.AC_BASH -> acBash
            Attribute.AC_SLASH -> acSlash
            Attribute.AC_PIERCE -> acPierce
            Attribute.AC_MAGIC -> acMagic
        }
    }

    fun hasAC(): Boolean {
        return acBash > 0 || acSlash > 0 || acPierce > 0 || acMagic > 0
    }
}

fun setAttribute(builder: AttributesBuilder, attribute: Attribute, value: Int): AttributesBuilder {
    when (attribute) {
        Attribute.HP -> builder.hp(value)
        Attribute.MANA -> builder.mana(value)
        Attribute.MV -> builder.mv(value)
        Attribute.STR -> builder.strength(value)
        Attribute.INT -> builder.intelligence(value)
        Attribute.WIS -> builder.wisdom(value)
        Attribute.DEX -> builder.dexterity(value)
        Attribute.CON -> builder.constitution(value)
        Attribute.HIT -> builder.hit(value)
        Attribute.DAM -> builder.dam(value)
        Attribute.AC_BASH -> builder.acBash(value)
        Attribute.AC_SLASH -> builder.acSlash(value)
        Attribute.AC_PIERCE -> builder.acPierce(value)
        Attribute.AC_MAGIC -> builder.acMagic(value)
    }
    return builder
}

class AttributeSetter(private val builder: AttributesBuilder) {
    fun set(attribute: Attribute, value: Int): AttributeSetter {
        setAttribute(builder, attribute, value)
        return this
    }

    fun build(): Attributes {
        return builder.build()
    }
}

fun isVitals(attribute: Attribute): Boolean {
    return attribute == Attribute.HP || attribute == Attribute.MANA || attribute == Attribute.MV
}
