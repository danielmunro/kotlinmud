package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemBuilderBuilder
import kotlinmud.item.ItemType
import kotlinmud.world.ResourceType

class PineTree : Resource {
    override val resourceType: ResourceType = ResourceType.PINE_TREE
    override val growable: Boolean = true
    override val toughness: Int = 2
    override fun createProduct(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a lump of coal")
            .description("a lump of coal is here")
            .type(ItemType.WOOD)
            .build()
    }
}
