package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenSwordRecipe : Recipe {
    override val name = "wooden sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a wooden sword")
                .description("a wooden sword is here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.WOOD)
                .damageType(DamageType.SLASH)
                .attackVerb("slash")
                .attributes(
                    mapOf(
                        Pair(Attribute.HIT, 1),
                        Pair(Attribute.DAM, 1),
                    )
                )
                .build()
        )
    }
}
