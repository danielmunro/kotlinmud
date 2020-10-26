package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class BlackberryBush : Resource {
    override val resourceType = ResourceType.BRUSH
    override val growable = true
    override val maturity = 5
    override val consumesResource = false
    override val toughness = 1

    override fun createProduct(): List<ItemDAO> {
        return randomAmount(3) { createItem() }
    }

    private fun createItem(): ItemDAO {
        return ItemDAO.new {
            name = "a handful of sweet blackberries"
            description = "a delicious handful of sun-ripened blackberries are here."
            type = ItemType.FOOD
        }
    }
}
