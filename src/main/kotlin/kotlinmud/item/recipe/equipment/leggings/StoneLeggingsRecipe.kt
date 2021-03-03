package kotlinmud.item.recipe.equipment.leggings

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.builder.ItemBuilder
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
            ItemBuilder(itemService)
                .name("cobblestone leggings")
                .description("cobblestone leggings are here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.STONE)
                .position(Position.LEGS)
                .attributes(
                        mapOf(
                                Pair(Attribute.AC_BASH, 1),
                                Pair(Attribute.AC_SLASH, 1),
                                Pair(Attribute.AC_PIERCE, 1),
                        )
                )
                .build()
        )
    }
}
