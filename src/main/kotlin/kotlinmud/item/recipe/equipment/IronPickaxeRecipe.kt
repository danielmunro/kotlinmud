package kotlinmud.item.recipe.equipment

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class IronPickaxeRecipe : Recipe {
    override val name = "iron pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.IRON_INGOT, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "an iron pick axe"
                description = "an iron pick axe is here."
                type = ItemType.EQUIPMENT
                material = Material.IRON
                position = Position.WEAPON
                damageType = DamageType.PIERCE
                attackVerb = "stab"
                attributes = AttributesDAO.new {
                    hit = 2
                    dam = 3
                }
            }
        )
    }
}
