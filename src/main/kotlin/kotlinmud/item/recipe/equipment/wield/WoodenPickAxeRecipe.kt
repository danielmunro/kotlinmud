package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType

class WoodenPickAxeRecipe : Recipe {
    override val name = "wooden pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a wooden pick axe",
                "a wooden pick axe is here.",
                3.0,
            ).makeWeapon(
                Weapon.EXOTIC,
                DamageType.PIERCE,
                "stab",
                Material.WOOD,
                1,
                1,
            ).build()
        )
    }
}
