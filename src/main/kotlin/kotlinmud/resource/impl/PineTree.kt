package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class PineTree : Resource {
    override val resourceType = ResourceType.PINE_TREE
    override val growable = true
    override val consumesResource = true
    override val toughness = 2

    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a pine tree log"
                description = "a pine tree log is here"
                type = ItemType.WOOD_LOG
            }
        )
    }
}