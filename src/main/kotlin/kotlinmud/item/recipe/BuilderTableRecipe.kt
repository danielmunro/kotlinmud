package kotlinmud.item.recipe

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class BuilderTableRecipe : Recipe {
    override val name: String = "a builder's table"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.LUMBER, 4))
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a builder's table")
                .description("A sturdy builder's table is here, crafted from fine wood with care.")
                .type(ItemType.BUILDER_TABLE)
                .material(Material.WOOD)
                .weight(20.0)
                .level(1)
                .worth(1)
                .build()
        )
    }
}
