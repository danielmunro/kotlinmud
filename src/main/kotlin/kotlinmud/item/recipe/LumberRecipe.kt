package kotlinmud.item.recipe

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class LumberRecipe : Recipe {
    override val name: String = "lumber"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.WOOD, 1))
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "lumber"
                it.description = "lumber is here, stacked in a clean pile"
                it.material = Material.WOOD
                it.weight = 10.0
                it.level = 1
                it.worth = 1
            }.build()
        )
    }
}
