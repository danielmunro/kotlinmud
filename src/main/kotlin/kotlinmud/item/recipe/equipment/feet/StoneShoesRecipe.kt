package kotlinmud.item.recipe.equipment.feet

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
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

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("stone shoes")
                .description("stone shoes are here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.STONE)
                .position(Position.FEET)
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
