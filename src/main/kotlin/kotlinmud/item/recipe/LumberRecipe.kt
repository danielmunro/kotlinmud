package kotlinmud.item.recipe

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class LumberRecipe : Recipe {
    override val name: String = "lumber"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.WOOD, 1))
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                type = ItemType.LUMBER
                name = "lumber"
                description = "lumber is here, stacked in a clean pile"
                material = Material.WOOD
                weight = 10.0
                level = 1
                worth = 1
            }
        )
    }
}
