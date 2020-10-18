package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class WoodenAxeRecipe : Recipe {
    override val name = "wooden axe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a wooden axe"
                description = "a wooden axe is here."
                type = ItemType.EQUIPMENT
                material = Material.WOOD
                position = Position.WEAPON
                damageType = DamageType.SLASH
                attackVerb = "chop"
                attributes = AttributesDAO.new {
                    hit = 1
                    dam = 1
                }
            }
        )
    }
}
