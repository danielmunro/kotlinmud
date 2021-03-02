package kotlinmud.item.recipe.equipment.helmet

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class StoneHelmetRecipe : Recipe {
    override val name = "stone helmet"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a cobblestone helmet")
                .description("a cobblestone helmet is here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.STONE)
                .position(Position.HEAD)
                .attributes(
                    AttributesDAO.new {
                        acBash = 1
                        acSlash = 1
                        acPierce = 1
                    }
                )
                .build()
        )
    }
}
