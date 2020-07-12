package kotlinmud.world.resource

import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType

class IronOre : Resource {
    override val resourceType: ResourceType = ResourceType.IRON_ORE
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun createProduct(): List<ItemDAO> {
        return listOf(
            ItemDAO.new {
                name = "a chunk of rock with hints of iron"
                description = "iron ore rock is here"
                type = ItemType.IRON_ORE
                attributes = AttributesDAO.new {}
            }
        )
    }
}
