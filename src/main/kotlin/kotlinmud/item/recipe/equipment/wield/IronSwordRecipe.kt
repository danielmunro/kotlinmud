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

class IronSwordRecipe : Recipe {
    override val name = "iron sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.IRON_INGOT, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("an iron sword")
                .description("an iron sword is here.")
                .type(ItemType.EQUIPMENT)
                .position(Position.WEAPON)
                .material(Material.IRON)
                .damageType(DamageType.SLASH)
                .attackVerb("slash")
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
