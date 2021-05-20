package kotlinmud.item.recipe.equipment.chest

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class StoneChestPlateRecipe : Recipe {
    override val name = "stone chest plate"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.COBBLESTONE, 5)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "a cobblestone chestplate"
                it.description = "a cobblestone chestplate is here."
                it.type = ItemType.EQUIPMENT
                it.material = Material.STONE
                it.position = Position.TORSO
                it.attributes = mapOf(
                    Pair(Attribute.AC_BASH, 1),
                    Pair(Attribute.AC_SLASH, 1),
                    Pair(Attribute.AC_PIERCE, 1),
                )
            }.build()
        )
    }
}
