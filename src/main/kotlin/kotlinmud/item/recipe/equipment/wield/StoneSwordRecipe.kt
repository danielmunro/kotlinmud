package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.item.type.Weapon
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
            itemService.builder(
                "a stone sword",
                "a stone sword is here.",
                6.0,
            ).makeWeapon(
                Weapon.SWORD,
                DamageType.SLASH,
                "slash",
                Material.STONE,
                1,
                2
            ).also {
                it.level = 2
            }.build()
        )
    }
}
