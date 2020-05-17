package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemType
import kotlinmud.service.ItemBuilderBuilder
import kotlinmud.world.ResourceType

class Tar : Resource {
    override val resourceType: ResourceType = ResourceType.TAR
    override val growable: Boolean = false
    override val toughness: Int = 2
    override fun produces(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a lump of tar")
            .description("a lump of tar is here")
            .type(ItemType.TAR)
            .build()
    }
}
