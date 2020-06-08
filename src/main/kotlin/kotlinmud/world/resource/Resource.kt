package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder

interface Resource {
    val resourceType: ResourceType
    val growable: Boolean
    val toughness: Int
    fun createProduct(builder: ItemBuilderBuilder): List<Item>
}
