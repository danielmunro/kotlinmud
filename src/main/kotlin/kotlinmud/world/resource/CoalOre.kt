package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemType
import kotlinmud.service.ItemBuilderBuilder
import kotlinmud.world.ResourceType

class CoalOre : Resource {
    override val resourceType: ResourceType = ResourceType.COAL_ORE
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun produces(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a lump of coal")
            .description("a lump of coal is here")
            .type(ItemType.COAL_LUMP)
            .build()
    }
}
