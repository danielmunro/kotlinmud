package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.availableInventoryAndItem
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

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
