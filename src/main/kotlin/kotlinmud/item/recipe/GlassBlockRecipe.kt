package kotlinmud.item.recipe

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class GlassBlockRecipe : Recipe {
    override val name: String = "glass block"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.SAND, 1)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                type = ItemType.GLASS_BLOCK
                name = "a block of glass"
                description = "a block of glass is here."
                material = Material.GLASS
                weight = 10.0
                level = 1
                worth = 1
            }
        )
    }
}
