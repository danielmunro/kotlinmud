package kotlinmud.item.type

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item

interface Recipe {
    val name: String
    fun getComponents(): Map<ItemType, Int>
    fun getProducts(): List<ItemDAO>
}
