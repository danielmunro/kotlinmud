package kotlinmud.item.recipe.equipment.wield

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe
import kotlinmud.mob.fight.type.DamageType

class StonePickaxeRecipe : Recipe {
    override val name = "stone pickaxe"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 2),
            Pair(ItemType.COBBLESTONE, 3)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a stone pick axe"
                description = "a stone pick axe is here."
                type = ItemType.EQUIPMENT
                material = Material.STONE
                position = Position.WEAPON
                damageType = DamageType.PIERCE
                attackVerb = "stab"
                attributes = AttributesDAO.new {
                    hit = 1
                    dam = 2
                }
            }
        )
    }
}
