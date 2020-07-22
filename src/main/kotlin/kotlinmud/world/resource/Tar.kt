package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType

class Tar : Resource {
    override val resourceType: ResourceType = ResourceType.TAR
    override val growable: Boolean = false
    override val toughness: Int = 2
    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a lump of tar"
                description = "a lump of tar is here, make sure not to touch it!"
                type = ItemType.TAR
            }
        )
    }
}
