package kotlinmud.item.recipe.equipment.chest

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class StoneChestPlateRecipe : Recipe {
    override val name = "stone chest plate"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.COBBLESTONE, 5)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
                ItemBuilder(itemService)
                        .name("a stone chest plate")
                        .description("a stone chest plate is here.")
                        .type(ItemType.EQUIPMENT)
                        .material(Material.STONE)
                        .position(Position.TORSO)
                        .attributes(AttributesDAO.new {
                            acBash = 1
                            acSlash = 1
                            acPierce = 1
                        })
                        .build()
        )
    }
}
