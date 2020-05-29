package kotlinmud.item.recipe

import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.Recipe

class Lumber : Recipe {
    override val name: String = "lumber"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.WOOD, 1))
    }

    override fun getProducts(): List<Item> {
        return listOf(
            ItemBuilder()
                .id(0)
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
