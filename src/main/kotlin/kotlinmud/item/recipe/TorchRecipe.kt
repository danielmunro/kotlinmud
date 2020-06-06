package kotlinmud.item.recipe

import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.item.type.Recipe

class TorchRecipe : Recipe {
    override val name: String = "torch"

    override fun getComponents(): Map<ItemType, Int> {
        return mapOf(
            Pair(ItemType.STICK, 1),
            Pair(ItemType.COAL_LUMP, 1)
        )
    }

    override fun getProducts(): List<Item> {
        return listOf(
            ItemBuilder()
                .id(0)
                .type(ItemType.EQUIPMENT)
                .position(Position.HOLD)
                .affects(mutableListOf(AffectInstance(AffectType.GLOWING)))
                .name("a torch")
                .description("a torch flickers gently")
                .material(Material.WOOD)
                .build()
        )
    }

}
