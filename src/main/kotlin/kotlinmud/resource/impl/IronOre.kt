package kotlinmud.resource.impl

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class IronOre : Resource {
    override val resourceType = ResourceType.IRON_ORE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a chunk of rock with hints of iron"
                description = "iron ore rock is here"
                type = ItemType.IRON_ORE
                attributes = AttributesDAO.new {}
            }
        )
    }
}
