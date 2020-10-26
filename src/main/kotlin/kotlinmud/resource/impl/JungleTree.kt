package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class JungleTree : Resource {
    override val resourceType = ResourceType.JUNGLE_TREE
    override val growable = true
    override val maturity = 10
    override val consumesResource = true
    override val toughness = 2
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
