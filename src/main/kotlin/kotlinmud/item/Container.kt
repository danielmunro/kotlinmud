package kotlinmud.item

import kotlinmud.attributes.Attributes

class Container(
    name: String,
    description: String,
    weight: Double = 1.0,
    attributes: Attributes = Attributes(),
    val inventory: Inventory = Inventory()
) : Item(name, description, weight, attributes)
