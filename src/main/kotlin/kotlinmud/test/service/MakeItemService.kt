package kotlinmud.test.service

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.model.Mob

class MakeItemService(private val itemService: ItemService, private val amount: Int) {
    var item: Item? = null

    fun lumber(): MakeItemService {
        return this
    }

    fun andGiveTo(mob: Mob) {
        for (i in 1..amount) {
            mob.items.add(createItem())
        }
    }

    private fun createItem(): Item {
        return itemService.builder(
            "lumber",
            "Fine pine lumber is here"
        ).also {
            it.type = ItemType.LUMBER
            it.material = Material.WOOD
        }.build()
    }
}
