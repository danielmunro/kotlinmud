package kotlinmud.item.recipe.equipment

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class IronSwordRecipe : Recipe {
    override val name = "iron sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.IRON_INGOT, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "an iron sword"
                description = "an iron sword is here."
                type = ItemType.EQUIPMENT
                position = Position.WEAPON
                material = Material.IRON
                damageType = DamageType.SLASH
                attackVerb = "slash"
                attributes = AttributesDAO.new {
                    hit = 2
                    dam = 3
                }
            }
        )
    }
}
