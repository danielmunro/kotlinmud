package kotlinmud.world.resource

import kotlinmud.biome.type.ResourceType
import kotlinmud.item.dao.ItemDAO

interface Resource {
    val resourceType: ResourceType
    val growable: Boolean
    val toughness: Int
    fun createProduct(): List<ItemDAO>
}
