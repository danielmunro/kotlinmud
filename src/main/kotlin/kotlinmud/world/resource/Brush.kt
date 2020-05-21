package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemType
import kotlinmud.service.ItemBuilderBuilder
import kotlinmud.world.ResourceType

class Brush : Resource {
    override val resourceType: ResourceType = ResourceType.BRUSH
    override val growable: Boolean = false
    override val toughness: Int = 1
    override fun createProduct(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("small green seeds")
            .description("a handful of small green seeds are here")
            .type(ItemType.GRASS_SEED)
            .build()
    }
}