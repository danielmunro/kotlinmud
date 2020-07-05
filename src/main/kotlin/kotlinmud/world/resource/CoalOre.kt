package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder
import kotlinmud.item.type.ItemType

class CoalOre : Resource {
    override val resourceType: ResourceType = ResourceType.COAL_ORE
    override val growable: Boolean = false
    override val toughness: Int = 3

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
