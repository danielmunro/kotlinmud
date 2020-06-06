package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.itemInInventoryAndAvailableInventory
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createGetFromItemAction(): Action {
    return Action(
        Command.GET,
        mustBeAwake(),
        itemInInventoryAndAvailableInventory()
    ) {
        val item = it.get<Item>(Syntax.ITEM_IN_AVAILABLE_INVENTORY)
        val itemWithInventory = it.get<Item>(Syntax.AVAILABLE_ITEM_INVENTORY)
        it.changeItemOwner(item, it.getMob())
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you get $item from $itemWithInventory.")
                .toObservers("${it.getMob()} gets $item from $itemWithInventory.")
                .build()
        )
    }
}
