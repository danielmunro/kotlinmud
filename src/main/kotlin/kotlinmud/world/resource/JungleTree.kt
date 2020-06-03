package kotlinmud.world.resource

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.world.ResourceType

class JungleTree : Resource {
    override val resourceType: ResourceType = ResourceType.JUNGLE_TREE
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
