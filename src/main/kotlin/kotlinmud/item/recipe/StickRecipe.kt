package kotlinmud.item.recipe

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
        val itemBuilder = itemService.builder(
            "a stick",
            "a stick, used for construction",
            0.1,
        ).also {
            it.type = ItemType.STICK
            it.material = Material.WOOD
        }
        val items = mutableListOf<Item>()
        for (i in 1..8) {
            items.add(itemBuilder.build())
        }
        return items
    }
}
