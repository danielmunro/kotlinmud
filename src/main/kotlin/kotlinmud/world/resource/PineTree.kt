package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType

class PineTree : Resource {
    override val resourceType: ResourceType = ResourceType.PINE_TREE
    override val growable: Boolean = true
    override val toughness: Int = 2
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
