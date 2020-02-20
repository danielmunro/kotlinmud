package kotlinmud.item

import kotlinmud.attributes.Attributes

class Item(
    val name: String,
    val description: String,
    val weight: Double = 1.0,
    val attributes: Attributes = Attributes())