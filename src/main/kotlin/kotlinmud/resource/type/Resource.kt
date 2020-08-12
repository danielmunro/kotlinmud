package kotlinmud.resource.type

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO

interface Resource {
    val resourceType: ResourceType
    val growable: Boolean
    val consumesResource: Boolean
    val toughness: Int
    fun createProduct(): List<ItemDAO>
}
