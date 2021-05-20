package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.type.Attribute
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenPickAxeRecipe : Recipe {
    override val name = "wooden pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 3)
        )
    }

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
            itemService.builder().also {
                it.name = "a wooden pick axe"
                it.description = "a wooden pick axe is here."
                it.type = ItemType.EQUIPMENT
                it.material = Material.WOOD
                it.position = Position.WEAPON
                it.damageType = DamageType.PIERCE
                it.attackVerb = "stab"
                it.attributes = mapOf(
                    Pair(Attribute.HIT, 1),
                    Pair(Attribute.DAM, 1),
                )
            }.build()
        )
    }
}
