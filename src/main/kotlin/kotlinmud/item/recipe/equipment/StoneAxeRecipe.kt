package kotlinmud.item.recipe.equipment

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class StoneAxeRecipe : Recipe {
    override val name = "stone axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a stone axe"
                description = "a stone axe is here."
                type = ItemType.EQUIPMENT
                material = Material.STONE
                position = Position.WEAPON
                damageType = DamageType.SLASH
                attackVerb = "chop"
                attributes = AttributesDAO.new {
                    hit = 1
                    dam = 2
                }
            }
        )
    }
}
