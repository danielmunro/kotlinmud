package kotlinmud.item

import kotlinmud.Noun
import kotlinmud.attributes.Attributes
import kotlinmud.attributes.HasAttributes

open class Item(
    override val name: String,
    val description: String,
    val weight: Double = 1.0,
    override val attributes: Attributes = Attributes()
) : HasAttributes, Noun
