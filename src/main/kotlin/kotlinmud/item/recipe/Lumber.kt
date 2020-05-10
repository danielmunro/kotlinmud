package kotlinmud.item.recipe

import kotlinmud.item.Item
import kotlinmud.item.ItemBuilder
import kotlinmud.item.ItemType
import kotlinmud.item.Material
import kotlinmud.item.Recipe

class Lumber : Recipe {
    override val name: String = "lumber"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.WOOD, 1))
    }

    override fun getProducts(): List<Item> {
        return listOf(
            ItemBuilder()
                .id(2)
                .type(ItemType.LUMBER)
                .name("lumber")
                .description("Lumber has been processed and ready to use.")
                .material(Material.WOOD)
                .weight(10.0)
                .level(1)
                .worth(1)
                .build()
        )
    }
}
