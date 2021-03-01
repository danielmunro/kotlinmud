package kotlinmud.test

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.mob.model.Mob
import org.jetbrains.exposed.sql.transactions.transaction

class MakeItemService(private val itemService: ItemService, private val amount: Int) {
    var item: Item? = null

    fun lumber(): MakeItemService {
        return this
    }

    fun andGiveTo(mob: Mob) {
        transaction {
            for (i in 1..amount) {
                mob.items.add(createItem())
            }
        }
    }

    private fun createItem(): Item {
        return ItemBuilder(itemService)
            .name("lumber")
            .description("Fine pine lumber i here")
            .type(ItemType.LUMBER)
            .build()
    }
}
