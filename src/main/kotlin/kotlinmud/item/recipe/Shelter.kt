package kotlinmud.item.recipe

import kotlinmud.item.Item
import kotlinmud.item.ItemBuilder
import kotlinmud.item.ItemType
import kotlinmud.item.Material
import kotlinmud.item.Recipe

class Shelter : Recipe {
    override val name: String = "shelter"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.LUMBER, 8)
        )
    }

    override fun getProducts(): List<Item> {
        return listOf(
            ItemBuilder()
                .id(0)
                .type(ItemType.FURNITURE)
                .name("a lean-to shelter")
                .description("a lean-to has been constructed hastily here.")
                .material(Material.WOOD)
                .canOwn(false)
                .build()
        )
    }
}