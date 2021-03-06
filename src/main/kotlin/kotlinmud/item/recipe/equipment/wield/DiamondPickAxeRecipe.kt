package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.item.type.Weapon
import kotlinmud.mob.fight.type.DamageType

class DiamondPickAxeRecipe : Recipe {
    override val name = "diamond pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.DIAMOND, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a diamond pick axe",
                "a diamond pick axe is here.",
                5.0,
            ).makeWeapon(
                Weapon.AXE,
                DamageType.PIERCE,
                "stab",
                Material.DIAMOND,
                3,
                4
            ).also {
                it.level = 15
                it.worth = 25
            }.build()
        )
    }
}
