package kotlinmud.test

import kotlinmud.item.HasInventory
import kotlinmud.item.ItemBuilder
import kotlinmud.item.ItemType

class MakeItemService(private val testService: TestService, private val amount: Int) {
    private val itemTemplate: ItemBuilder = testService.itemBuilder()

    fun lumber(): MakeItemService {
        itemTemplate.type(ItemType.LUMBER)
        return this
    }

    fun andGiveTo(hasInventory: HasInventory) {
        for (i in 1..amount) {
            testService.buildItem(itemTemplate, hasInventory)
        }
    }
}
