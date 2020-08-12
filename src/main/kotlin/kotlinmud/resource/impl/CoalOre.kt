package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class CoalOre : Resource {
    override val resourceType = ResourceType.COAL_ORE
    override val growable = false
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a lump of coal"
                description = "a lump of coal is here"
                type = ItemType.COAL_LUMP
            }
        )
    }
}
