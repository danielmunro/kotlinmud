package kotlinmud.item.recipe.shelter

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class SleepingBagRecipe : Recipe {
    override val name: String = "a sleeping bag"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.WOOL, 4)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.type = ItemType.FURNITURE
                it.name = "a sleeping bag"
                it.description = "a warm and comfortable sleeping bag is here."
                it.material = Material.TEXTILE
            }.build()
        )
    }
}
