package kotlinmud.item.recipe

import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
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
            itemService.builder(
                "a torch",
                "a torch flickers gently."
            ).also {
                it.type = ItemType.EQUIPMENT
                it.material = Material.WOOD
                it.position = Position.HELD
                it.affects = listOf(
                    Affect(AffectType.GLOWING)
                )
            }.build()
        )
    }
}
