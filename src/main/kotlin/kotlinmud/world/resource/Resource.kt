package kotlinmud.world.resource

import kotlinmud.item.ItemType

interface Resource {
    val growable: Boolean
    val toughness: Int
    val producesItemType: ItemType
}
