package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemBuilderBuilder
import kotlinmud.item.ItemType
import kotlinmud.world.ResourceType

class GoldOre : Resource {
    override val resourceType: ResourceType = ResourceType.GOLD_ORE
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun createProduct(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a chunk of rock with hints of gold")
            .description("gold ore rock is here")
            .type(ItemType.GOLD_ORE)
            .build()
    }
}
