package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder
import kotlinmud.item.type.ItemType

class JungleTree : Resource {
    override val resourceType: ResourceType = ResourceType.JUNGLE_TREE
    override val growable: Boolean = true
    override val toughness: Int = 2
    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a jungle tree log"
                description = "a jungle tree log is here"
                type = ItemType.WOOD_LOG
            }
        )
    }
}
