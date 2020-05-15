package kotlinmud.world.resource

import kotlinmud.item.ItemType

class Brush : Resource {
    override val growable: Boolean = false
    override val toughness: Int = 1
    override val producesItemType: ItemType = ItemType.GRASS_SEED
}
