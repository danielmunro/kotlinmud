package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class DiamondAxeRecipe : Recipe {
    override val name = "diamond axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.DIAMOND, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a diamond axe",
                "a diamond axe is here."
            ).makeWeapon(
                DamageType.SLASH,
                "chop",
                Material.DIAMOND,
                3,
                4,
            ).build()
        )
    }
}
