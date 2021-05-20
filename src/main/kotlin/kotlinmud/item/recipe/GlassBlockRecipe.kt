package kotlinmud.item.recipe

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class GlassBlockRecipe : Recipe {
    override val name: String = "glass block"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.SAND, 1)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "a block of glass"
                it.description = "a block of glass is here."
                it.type = ItemType.GLASS_BLOCK
                it.material = Material.GLASS
                it.weight = 3.0
                it.level = 1
                it.worth = 1
            }.build()
        )
    }
}
