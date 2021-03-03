package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class StonePickaxeRecipe : Recipe {
    override val name = "stone pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a stone pick axe")
                .description("a stone pick axe is here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.STONE)
                .position(Position.WEAPON)
                .attackVerb("stab")
                .attributes(
                        mapOf(
                                Pair(Attribute.HIT, 1),
                                Pair(Attribute.DAM, 2),
                        )
                )
                .build()
        )
    }
}
