package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemType
import kotlinmud.service.ItemBuilderBuilder
import kotlinmud.world.ResourceType

class JungleTree : Resource {
    override val resourceType: ResourceType = ResourceType.JUNGLE_TREE
    override val growable: Boolean = true
    override val toughness: Int = 2
    override fun produces(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a lump of coal")
            .description("a lump of coal is here")
            .type(ItemType.WOOD)
            .build()
    }
}
