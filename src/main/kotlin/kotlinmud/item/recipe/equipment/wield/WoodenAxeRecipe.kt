package kotlinmud.item.recipe.equipment.wield

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenAxeRecipe : Recipe {
    override val name = "wooden axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder(
                "a wooden axe",
                "a wooden axe is here."
            ).makeWeapon(
                DamageType.SLASH,
                "chop",
                Material.WOOD,
                1,
                1,
            ).build()
        )
    }
}
