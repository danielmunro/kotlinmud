package kotlinmud.item.recipe.shelter

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class ShelterRecipe : Recipe {
    override val name: String = "shelter"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.LUMBER, 8)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                type = ItemType.FURNITURE
                name = "a lean-to shelter"
                description = "a lean-to has been constructed hastily here."
                material = Material.WOOD
                canOwn = false
            }
        )
    }
}
