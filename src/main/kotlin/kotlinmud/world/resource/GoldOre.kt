package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType

class GoldOre : Resource {
    override val resourceType: ResourceType = ResourceType.GOLD_ORE
    override val growable: Boolean = false
    override val toughness: Int = 3

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
