package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class DiamondOre : Resource {
    override val resourceType = ResourceType.DIAMOND_ORE
    override val growable = false
    override val maturity: Int? = null
    override val consumesResource = true
    override val toughness = 4

    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a diamond"
                description = "a glittering diamond is here."
                type = ItemType.DIAMOND
            }
        )
    }
}
