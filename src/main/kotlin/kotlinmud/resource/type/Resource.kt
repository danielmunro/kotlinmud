package kotlinmud.resource.type

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService

interface Resource {
    val resourceType: ResourceType
    val growable: Boolean
    val maturity: Int?
    val consumesResource: Boolean
    val toughness: Int
    fun createProduct(itemService: ItemService): List<Item>
}
