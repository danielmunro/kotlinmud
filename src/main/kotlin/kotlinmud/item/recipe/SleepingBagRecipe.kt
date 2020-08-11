package kotlinmud.item.recipe

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class SleepingBagRecipe : Recipe {
    override val name: String = "a sleeping bag"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.WOOL, 4)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                type = ItemType.FURNITURE
                name = "a sleeping bag"
                description = "a warm and comfortable sleeping bag is here."
                material = Material.TEXTILE
            }
        )
    }
}
