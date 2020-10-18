package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
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

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a diamond axe"
                description = "a diamond axe is here."
                type = ItemType.EQUIPMENT
                material = Material.DIAMOND
                position = Position.WEAPON
                damageType = DamageType.SLASH
                attackVerb = "chop"
                attributes = AttributesDAO.new {
                    hit = 3
                    dam = 4
                }
            }
        )
    }
}
