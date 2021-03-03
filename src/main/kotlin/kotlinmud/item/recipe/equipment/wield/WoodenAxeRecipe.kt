package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenAxeRecipe : Recipe {
    override val name = "wooden axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a wooden axe")
                .description("a wooden axe is here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.WOOD)
                .damageType(DamageType.SLASH)
                .attackVerb("chop")
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
