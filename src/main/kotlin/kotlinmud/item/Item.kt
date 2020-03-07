package kotlinmud.item

import kotlinmud.Noun
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes
import kotlinmud.data.Row

class Item(
    override val id: Int,
    override val name: String,
    override val description: String,
    val value: Int,
    val level: Int,
    val weight: Double = 1.0,
    override val attributes: Attributes = Attributes(),
    val material: Material = Material.ORGANIC,
    val position: Position = Position.NONE,
    val inventory: Inventory? = null
) : HasAttributes, Noun, Row {
    override fun toString(): String {
        return name
    }

    fun copy(): Item {
        return Item(
            id,
            name,
            description,
            value,
            level,
            weight,
            attributes,
            material,
            position,
            inventory
        )
    }
}
