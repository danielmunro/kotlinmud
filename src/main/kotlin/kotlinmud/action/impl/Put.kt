package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.availableInventoryAndItem
import kotlinmud.item.Item

fun createPutAction(): Action {
    return Action(Command.PUT, mustBeAwake(), availableInventoryAndItem()) {
        val item = it.get<Item>(Syntax.ITEM_IN_INVENTORY)
        val availableItemInventory = it.get<Item>(Syntax.AVAILABLE_ITEM_INVENTORY)
        it.changeItemOwner(item, availableItemInventory)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you put $item into $availableItemInventory.")
                .toObservers("${it.getMob()} puts $item into $availableItemInventory.")
                .build()
        )
    }
}
