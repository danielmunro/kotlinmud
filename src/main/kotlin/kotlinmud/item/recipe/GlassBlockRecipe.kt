package kotlinmud.item.recipe

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
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

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
                ItemBuilder(itemService)
                        .name("a block of glass")
                        .description("a block of glass is here.")
                        .type(ItemType.GLASS_BLOCK)
                        .material(Material.GLASS)
                        .weight(3.0)
                        .level(1)
                        .worth(1)
                        .build()
        )
    }
}
