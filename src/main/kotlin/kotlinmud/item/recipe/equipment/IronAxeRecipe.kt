package kotlinmud.item.recipe.equipment

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class IronAxeRecipe : Recipe {
    override val name = "stone axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.IRON_INGOT, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "an iron axe"
                description = "an iron axe is here."
                type = ItemType.EQUIPMENT
                material = Material.IRON
                position = Position.WEAPON
                damageType = DamageType.SLASH
                attackVerb = "chop"
                attributes = AttributesDAO.new {
                    hit = 2
                    dam = 3
                }
            }
        )
    }
}
