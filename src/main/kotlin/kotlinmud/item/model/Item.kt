package kotlinmud.item.model

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.attributes.type.HasAttributes
import kotlinmud.helper.Identifiable
import kotlinmud.helper.Noun
import kotlinmud.item.type.*
import kotlinmud.mob.fight.type.DamageType

class Item(
        val type: ItemType,
        override val name: String,
        override val description: String,
        val worth: Int,
        val level: Int,
        val weight: Double,
        val material: Material,
        val isContainer: Boolean,
        val canOwn: Boolean,
        val affects: MutableList<Affect> = mutableListOf(),
        val canonicalId: ItemCanonicalId?,
        val position: Position?,
        val attackVerb: String?,
        val damageType: DamageType?,
        val drink: Drink?,
        val food: Food?,
        var quantity: Int?,
        val decayTimer: Int?,
        val maxItems: Int?,
        val maxWeight: Int?,
        override val attributes: MutableMap<Attribute, Int>,
        val items: MutableList<Item>?,
) : HasAttributes, Identifiable, Noun {

    fun isVisible(): Boolean {
        return affects.find { it.type == AffectType.INVISIBILITY } == null
    }

    override fun toString(): String {
        return name
    }
}
