package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class StoneSwordRecipe : Recipe {
    override val name = "stone sword"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a stone sword"
                description = "a stone sword is here."
                type = ItemType.EQUIPMENT
                position = Position.WEAPON
                material = Material.STONE
                damageType = DamageType.SLASH
                attackVerb = "slash"
                attributes = AttributesDAO.new {
                    hit = 1
                    dam = 2
                }
            }
        )
    }
}
