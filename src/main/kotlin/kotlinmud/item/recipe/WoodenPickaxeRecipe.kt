package kotlinmud.item.recipe

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenPickaxeRecipe : Recipe {
    override val name = "wooden pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a wooden pick axe"
                description = "a wooden pick axe is here."
                type = ItemType.EQUIPMENT
                material = Material.WOOD
                position = Position.WEAPON
                damageType = DamageType.PIERCE
                attackVerb = "stab"
                attributes = AttributesDAO.new {
                    hit = 1
                    dam = 1
                }
            }
        )
    }
}
