package kotlinmud.item.recipe.equipment.feet

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class StoneShoesRecipe : Recipe {
    override val name = "stone shoes"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "stone shoes"
                description = "stone shoes are here."
                type = ItemType.EQUIPMENT
                material = Material.STONE
                position = Position.FEET
                attributes = AttributesDAO.new {
                    acBash = 1
                    acSlash = 1
                    acPierce = 1
                    acMagic = 0
                }
            }
        )
    }
}
