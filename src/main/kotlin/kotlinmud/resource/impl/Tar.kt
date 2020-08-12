package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class Tar : Resource {
    override val resourceType = ResourceType.TAR
    override val growable = false
    override val consumesResource = true
    override val toughness = 2

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
