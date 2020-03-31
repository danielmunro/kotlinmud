package kotlinmud.item

import com.thinkinglogic.builder.annotation.Builder
import com.thinkinglogic.builder.annotation.DefaultValue
import com.thinkinglogic.builder.annotation.Mutable
import com.thinkinglogic.builder.annotation.NullableType
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
    val worth: Int,
    val level: Int,
    val weight: Double = 1.0,
    override val attributes: Attributes,
    val material: Material,
    val position: Position,
    @DefaultValue("") val attackVerb: String,
    @Mutable override val affects: MutableList<AffectInstance>,
    @NullableType val inventory: Inventory?,
    val drink: Drink,
    val food: Food,
    var quantity: Int
) : HasAttributes, Noun, Row {
    override fun toString(): String {
        return name
    }

    override fun affects(): AffectService {
        return AffectService(this)
    }

    fun copy(): Item {
        return Item(
            id,
            name,
            description,
            worth,
            level,
            weight,
            attributes.copy(),
            material,
            position,
            attackVerb,
            mutableListOf(),
            Inventory(),
            drink,
            food,
            quantity
        )
    }
}
