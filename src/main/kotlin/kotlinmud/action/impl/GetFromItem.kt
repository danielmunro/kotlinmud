package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.itemInInventoryAndAvailableInventory
import kotlinmud.item.Item

fun createGetFromItemAction(): Action {
    return Action(Command.GET, mustBeAwake(), itemInInventoryAndAvailableInventory()) {
        val item = it.get<Item>(Syntax.ITEM_IN_AVAILABLE_INVENTORY)
        val itemWithInventory = it.get<Item>(Syntax.AVAILABLE_ITEM_INVENTORY)
        it.changeItemOwner(item, it.getMob())
        it.createResponse(
            MessageBuilder()
                .toActionCreator("you get $item from $itemWithInventory.")
                .toObservers("${it.getMob()} gets $item from $itemWithInventory.")
                .build()
        )
    }
}
