package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class IronAxeRecipe : Recipe {
    override val name = "stone axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.IRON_INGOT, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("an iron axe")
                .description("an iron axe is here.")
                .type(ItemType.EQUIPMENT)
                .material(Material.IRON)
                .position(Position.WEAPON)
                .damageType(DamageType.SLASH)
                .attackVerb("chop")
                .attributes(
                    mapOf(
                        Pair(Attribute.HIT, 2),
                        Pair(Attribute.DAM, 3),
                    )
                )
                .build()
        )
    }
}
