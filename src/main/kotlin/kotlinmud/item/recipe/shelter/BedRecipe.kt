package kotlinmud.item.recipe.shelter

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class BedRecipe : Recipe {
    override val name: String = "bed"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.LUMBER, 6),
            Pair(ItemType.COTTON, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.type = ItemType.FURNITURE
                it.name = "a comfortable bed is here."
                it.description = "a comfortable bed is here, maybe you should lie down and rest."
                it.material = Material.WOOD
                it.canOwn = false
            }.build()
        )
    }
}
