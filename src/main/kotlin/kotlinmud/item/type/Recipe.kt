package kotlinmud.item.type

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService

interface Recipe {
    val name: String
    fun getComponents(): Map<ItemType, Int>
    fun getProducts(itemService: ItemService): List<Item>
}
