package kotlinmud.resource.impl

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.resource.type.Resource

class GoldOre : Resource {
    override val resourceType = ResourceType.GOLD_ORE
    override val growable = false
    override val consumesResource = true
    override val toughness = 3

    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a chunk of rock with hints of gold"
                description = "gold ore rock is here"
                type = ItemType.GOLD_ORE
            }
        )
    }
}