package kotlinmud.world.resource

import kotlinmud.item.Item
import kotlinmud.item.ItemType
import kotlinmud.service.ItemBuilderBuilder

class GoldOre : Resource {
    override val growable: Boolean = false
    override val toughness: Int = 3

    override fun produces(builder: ItemBuilderBuilder): Item {
        return builder()
            .name("a chunk of rock with hints of gold")
            .description("gold ore rock is here")
            .type(ItemType.GOLD_ORE)
            .build()
    }
}
