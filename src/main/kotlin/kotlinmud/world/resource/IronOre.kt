package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemType
import kotlinmud.service.ItemBuilderBuilder
import kotlinmud.world.ResourceType

class IronOre : Resource {
    override val resourceType: ResourceType = ResourceType.IRON_ORE
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun createProduct(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a chunk of rock with hints of iron")
            .description("iron ore rock is here")
            .type(ItemType.IRON_ORE)
            .build()
    }
}
