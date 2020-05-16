package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.itemInInventoryToAvailableInventory
import kotlinmud.item.Item

fun createPutAction(): Action {
    return Action(Command.PUT, mustBeAwake(), itemInInventoryToAvailableInventory()) {
        val item = it.get<Item>(Syntax.ITEM_IN_INVENTORY)
        val availableItemInventory = it.get<Item>(Syntax.AVAILABLE_ITEM_INVENTORY)
        it.changeItemOwner(item, availableItemInventory)
        it.createResponse(
            Message(
                "you put $item into $availableItemInventory.",
                "${it.getMob()} puts $item into $availableItemInventory."
            )
        )
    }
}
