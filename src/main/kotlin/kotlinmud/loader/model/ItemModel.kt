package kotlinmud.loader.model

import kotlinmud.attributes.Attributes
import kotlinmud.item.Inventory
import kotlinmud.item.Material
import kotlinmud.item.Position

class ItemModel(
    override val id: Int,
    val name: String,
    val description: String,
    val weight: Double = 1.0,
    val attributes: Attributes = Attributes(),
    val material: Material = Material.ORGANIC,
    val position: Position = Position.NONE,
    val inventory: Inventory? = null
) : Model
