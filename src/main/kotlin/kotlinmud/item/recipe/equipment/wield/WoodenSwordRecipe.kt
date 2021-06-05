package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.item.type.Weapon
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
            itemService.builder(
                "a wooden sword",
                "a wooden sword is here.",
                3.0,
            ).makeWeapon(
                Weapon.SWORD,
                DamageType.POUND,
                "twack",
                Material.WOOD,
                1,
                1,
            ).build()
        )
    }
}
