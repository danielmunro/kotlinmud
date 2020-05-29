package kotlinmud.item.recipe

import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.Recipe

class BuilderTable : Recipe {
    override val name: String = "a builder's table"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.LUMBER, 4))
    }

    override fun getProducts(): List<Item> {
        return listOf(
            ItemBuilder()
                .id(0)
                .type(ItemType.BUILDER_TABLE)
                .name("a builder's table")
                .description("A sturdy builder's table is here, crafted from fine wood with care.")
                .material(Material.WOOD)
                .weight(20.0)
                .level(1)
                .worth(1)
                .build()
        )
    }
}
