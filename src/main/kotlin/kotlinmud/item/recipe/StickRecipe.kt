package kotlinmud.item.recipe

import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class StickRecipe : Recipe {
    override val name: String = "sticks"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.LUMBER, 1))
    }

    override fun getProducts(): List<Item> {
        return listOf(
            stickItem(),
            stickItem(),
            stickItem(),
            stickItem(),
            stickItem(),
            stickItem()
        )
    }

    private fun stickItem(): Item {
        return ItemBuilder()
            .id(0)
            .type(ItemType.STICK)
            .name("a stick")
            .description("a stick, used for construction")
            .material(Material.WOOD)
            .build()
    }
}