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
)

fun isVitals(attribute: Attribute): Boolean {
    return attribute == Attribute.HP || attribute == Attribute.MANA || attribute == Attribute.MV
}
