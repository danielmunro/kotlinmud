package kotlinmud.item

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes

class Item(
    val name: String,
    val description: String,
    val weight: Double = 1.0,
    override val attributes: Attributes = Attributes()) : HasAttributes