package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class WildGrass : Resource {
    override val resourceType = ResourceType.BRUSH
    override val growable = true
    override val maturity = 2
    override val consumesResource = true
    override val toughness = 1

    override fun createProduct(): List<ItemDAO> {
        return randomAmount(3 + dice(1, maturity)) { createItem() }
    }

    private fun createItem(): ItemDAO {
        return ItemDAO.new {
            name = "small green seeds"
            description = "a handful of small green seeds are here"
            type = ItemType.GRASS_SEED
        }
    }
}
