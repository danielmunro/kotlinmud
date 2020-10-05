package kotlinmud.item.recipe.equipment

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenSwordRecipe : Recipe {
    override val name = "wooden sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a wooden sword"
                description = "a wooden sword is here."
                type = ItemType.EQUIPMENT
                position = Position.WEAPON
                material = Material.WOOD
                damageType = DamageType.SLASH
                attackVerb = "slash"
                attributes = AttributesDAO.new {
                    hit = 1
                    dam = 1
                }
            }
        )
    }
}
