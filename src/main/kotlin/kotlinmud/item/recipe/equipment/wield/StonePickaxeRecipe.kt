package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class StonePickaxeRecipe : Recipe {
    override val name = "stone pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a stone pick axe",
                "a stone pick axe is here.",
                6.0,
            ).makeWeapon(
                DamageType.PIERCE,
                "stab",
                Material.STONE,
                1,
                2,
            ).also {
                it.level = 2
            }.build()
        )
    }
}
