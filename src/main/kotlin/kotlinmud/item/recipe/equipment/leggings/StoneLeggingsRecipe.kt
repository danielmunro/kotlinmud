package kotlinmud.item.recipe.equipment.leggings

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class StoneLeggingsRecipe : Recipe {
    override val name = "stone leggings"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "cobblestone leggings",
                "cobblestone leggings are here.",
                4.0,
            ).also {
                it.type = ItemType.EQUIPMENT
                it.material = Material.STONE
                it.position = Position.LEGS
                it.attributes = mutableMapOf(
                    Pair(Attribute.AC_BASH, 1),
                    Pair(Attribute.AC_SLASH, 1),
                    Pair(Attribute.AC_PIERCE, 1),
                )
            }.build()
        )
    }
}
