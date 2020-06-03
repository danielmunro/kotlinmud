package kotlinmud.test

import kotlinmud.item.model.ItemBuilder
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemType

class MakeItemService(private val testService: TestService, private val amount: Int) {
    private val itemTemplate: ItemBuilder = testService.itemBuilder()

    fun lumber(): MakeItemService {
        itemTemplate.name("lumber")
            .description("Fine pine lumber is here.")
            .type(ItemType.LUMBER)
        return this
    }

    fun andGiveTo(hasInventory: HasInventory) {
        for (i in 1..amount) {
            testService.buildItem(itemTemplate, hasInventory)
        }
    }
}
