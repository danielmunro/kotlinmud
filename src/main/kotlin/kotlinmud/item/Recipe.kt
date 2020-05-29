package kotlinmud.item

import kotlinmud.item.model.Item
import kotlinmud.item.type.ItemType

interface Recipe {
    val name: String
    fun getComponents(): Map<ItemType, Int>
    fun getProducts(): List<Item>
}
