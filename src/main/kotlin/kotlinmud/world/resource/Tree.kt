package kotlinmud.world.resource

import kotlinmud.item.ItemType

class Tree : Resource {
    override val growable: Boolean = true
    override val toughness: Int = 2
    override val producesItemType: ItemType = ItemType.WOOD
}
