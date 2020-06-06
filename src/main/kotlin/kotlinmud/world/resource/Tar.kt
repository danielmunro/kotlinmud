package kotlinmud.world.resource

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.world.ResourceType

class Tar : Resource {
    override val resourceType: ResourceType = ResourceType.TAR
    override val growable: Boolean = false
    override val toughness: Int = 2
    override fun createProduct(builder: ItemBuilderBuilder): List<Item> {
        return listOf(
            builder()
                .name("a lump of tar")
                .description("a lump of tar is here")
                .type(ItemType.TAR)
                .build()
        )
    }
}
