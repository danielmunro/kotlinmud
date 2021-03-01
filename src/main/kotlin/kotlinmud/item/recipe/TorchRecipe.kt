package kotlinmud.item.recipe

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
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

    override fun getProducts(itemService: ItemService): List<Item> {
        return listOf(
                ItemBuilder(itemService)
                        .name("a torch")
                        .description("a torch flickers gently")
                        .type(ItemType.EQUIPMENT)
                        .position(Position.HEAD)
                        .material(Material.WOOD)
                        .affects(listOf(
                                Affect(AffectType.GLOWING)
                        ))
                        .build()
        )
    }
}
