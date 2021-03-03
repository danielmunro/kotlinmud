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

class StoneSwordRecipe : Recipe {
    override val name = "stone sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            ItemBuilder(itemService)
                .name("a stone sword")
                .description("a stone sword is here.")
                .type(ItemType.EQUIPMENT)
                .position(Position.WEAPON)
                .material(Material.STONE)
                .damageType(DamageType.SLASH)
                .attackVerb("slash")
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
