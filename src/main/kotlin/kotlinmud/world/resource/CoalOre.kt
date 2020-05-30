package kotlinmud.world.resource

import kotlinmud.item.ItemBuilderBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.type.ItemType
import kotlinmud.world.ResourceType

class CoalOre : Resource {
    override val resourceType: ResourceType = ResourceType.COAL_ORE
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun createProduct(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a lump of coal")
            .description("a lump of coal is here")
            .type(ItemType.COAL_LUMP)
            .build()
    }
}
