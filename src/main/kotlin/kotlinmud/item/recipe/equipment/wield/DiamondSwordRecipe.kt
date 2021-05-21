package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class DiamondSwordRecipe : Recipe {
    override val name = "diamond sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.DIAMOND, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a diamond sword",
                "a diamond sword is here."
            ).makeWeapon(
                DamageType.SLASH,
                "slash",
                Material.DIAMOND,
                5,
                6,
            ).build()
        )
    }
}
