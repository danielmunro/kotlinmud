package kotlinmud.world.resource

import kotlinmud.item.ItemType

class Coal : Resource {
    override val growable: Boolean = false
    override val toughness: Int = 3
    override val producesItemType: ItemType = ItemType.COAL
}
