package kotlinmud.item.recipe

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class StickRecipe : Recipe {
    override val name: String = "sticks"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.LUMBER, 1))
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        val itemBuilder = ItemBuilder(itemService)
            .name("a stick")
            .description("a stick, used for construction")
            .type(ItemType.STICK)
            .material(Material.WOOD)
        val items = mutableListOf<Item>()
        for (i in 1..8) {
            items.add(itemBuilder.build())
        }
        return items
    }
}
