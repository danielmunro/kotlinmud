package kotlinmud.item.model

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.attributes.type.HasAttributes
import kotlinmud.helper.Identifiable
import kotlinmud.helper.Noun
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell

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
    val spells: MutableList<SkillType> = mutableListOf(),
    val canonicalId: ItemCanonicalId?,
    val position: Position?,
    val attackVerb: String?,
    val damageType: DamageType?,
    val drink: Drink?,
    val food: Food?,
    var quantity: Int?,
    var decayTimer: Int?,
    val maxItems: Int?,
    val maxWeight: Int?,
    override val attributes: MutableMap<Attribute, Int>,
    val items: MutableList<Item>?,
) : HasAttributes, Identifiable, Noun {

    @JsonIgnore
    fun isVisible(): Boolean {
        return affects.find { it.type == AffectType.INVISIBILITY } == null
    }

    override fun toString(): String {
        return name
    }
}
