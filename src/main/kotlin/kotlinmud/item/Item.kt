package kotlinmud.item

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import kotlinmud.Noun
import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes
import kotlinmud.data.Row
import kotlinmud.service.AffectService

@Builder
class Item(
    override val id: Int,
    override val name: String,
    override val description: String,
    @DefaultValue("ItemType.OTHER") val type: ItemType,
    @DefaultValue("0") val worth: Int,
    val level: Int,
    val weight: Double = 1.0,
    @DefaultValue("Attributes()") override val attributes: Attributes,
    @DefaultValue("Material.ORGANIC") val material: Material,
    @DefaultValue("Position.NONE") val position: Position,
    @DefaultValue("") val attackVerb: String,
    @DefaultValue("mutableListOf()") @Mutable override val affects: MutableList<AffectInstance>,
    @DefaultValue("Drink.NONE") val drink: Drink,
    @DefaultValue("Food.NONE") val food: Food,
    @DefaultValue("0") var quantity: Int,
    @DefaultValue("-1") var decayTimer: Int,
    @DefaultValue("false") val hasInventory: Boolean
) : HasAttributes, Noun, Row, HasInventory {

    override fun toString(): String {
        return name
    }

    override fun affects(): AffectService {
        return AffectService(this)
    }

    fun copy(): Item {
        return ItemBuilder(this).build()
    }
}
