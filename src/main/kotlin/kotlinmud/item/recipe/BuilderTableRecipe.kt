package kotlinmud.item.recipe

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe

class BuilderTableRecipe : Recipe {
    override val name: String = "a builder's table"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.LUMBER, 4))
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a builder's table",
                "A sturdy builder's table is here, crafted from fine wood with care."
            ).also {
                it.type = ItemType.BUILDER_TABLE
                it.material = Material.WOOD
                it.weight = 20.0
                it.level = 1
                it.worth = 1
            }.build()
        )
    }
}
