package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.item.type.Weapon
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
            itemService.builder(
                "an iron axe",
                "an iron axe is here.",
                9.5,
            ).makeWeapon(
                Weapon.AXE,
                DamageType.SLASH,
                "chop",
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
