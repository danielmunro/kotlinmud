package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
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
            itemService.builder().also {
                it.name = "a wooden axe"
                it.description = "a wooden axe is here."
                it.type = ItemType.EQUIPMENT
                it.material = Material.WOOD
                it.damageType = DamageType.SLASH
                it.attackVerb = "chop"
                it.attributes = mapOf(
                    Pair(Attribute.HIT, 1),
                    Pair(Attribute.DAM, 1),
                )
            }.build()
        )
    }
}
