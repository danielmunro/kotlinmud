package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class IronPickAxeRecipe : Recipe {
    override val name = "iron pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.IRON_INGOT, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "an iron pick axe",
                "an iron pick axe is here.",
                9.5,
            ).makeWeapon(
                DamageType.PIERCE,
                "stab",
                Material.IRON,
                2,
                3,
            ).also {
                it.level = 8
                it.worth = 5
            }.build()
        )
    }
}
