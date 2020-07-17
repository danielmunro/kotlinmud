package kotlinmud.item.recipe

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Recipe
import org.jetbrains.exposed.sql.transactions.transaction

class BuilderTableRecipe : Recipe {
    override val name: String = "a builder's table"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(Pair(ItemType.LUMBER, 4))
    }

    override fun getProducts(): List<ItemDAO> {
        return transaction {
            listOf(
                ItemDAO.new {
                    type = ItemType.BUILDER_TABLE
                    name = "a builder's table"
                    description = "A sturdy builder's table is here, crafted from fine wood with care."
                    material = Material.WOOD
                    weight = 20.0
                    level = 1
                    worth = 1
                    attributes = AttributesDAO.new {}
                }
            )
        }
    }
}
