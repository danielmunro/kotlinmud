package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
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
            itemService.builder().also {
                it.name = "a diamond sword"
                it.description = "a diamond sword is here."
                it.makeWeapon(DamageType.SLASH, "slash")
                it.material = Material.DIAMOND
                it.attributes = mapOf(
                    Pair(Attribute.HIT, 5),
                    Pair(Attribute.DAM, 6),
                )
            }.build()
        )
    }
}
