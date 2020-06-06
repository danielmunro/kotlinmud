package kotlinmud.world.resource

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemBuilderBuilder
import kotlinmud.world.ResourceType

interface Resource {
    val resourceType: ResourceType
    val growable: Boolean
    val toughness: Int
    fun createProduct(builder: ItemBuilderBuilder): List<Item>
}
