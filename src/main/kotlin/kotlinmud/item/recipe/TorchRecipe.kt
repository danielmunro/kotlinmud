package kotlinmud.item.recipe

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.type.AffectType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class TorchRecipe : Recipe {
    override val name: String = "torch"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 1),
            Pair(ItemType.COAL_LUMP, 1)
        )
    }

    override fun getProducts(): List<ItemDAO> {
        val item = ItemDAO.new {
            type = ItemType.EQUIPMENT
            position = Position.HOLD
            name = "a torch"
            description = "a torch flickers gently"
            material = Material.WOOD
        }
        item.affects.plus(AffectDAO.new { type = AffectType.GLOWING })

        return listOf(item)
    }
}
