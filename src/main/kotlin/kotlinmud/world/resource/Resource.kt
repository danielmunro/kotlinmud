package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.service.ItemBuilderBuilder

interface Resource {
    val growable: Boolean
    val toughness: Int
    fun produces(builder: ItemBuilderBuilder): Item
}
