package kotlinmud.item

import kotlinmud.Noun
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes

class Item(
    val id: Int,
    override val name: String,
    override val description: String,
    val weight: Double = 1.0,
    override val attributes: Attributes = Attributes(),
    val position: Position = Position.NONE,
    val inventory: Inventory? = null
) : HasAttributes, Noun {
    override fun toString(): String {
        return name
    }
}
