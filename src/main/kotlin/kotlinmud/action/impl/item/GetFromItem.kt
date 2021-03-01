package kotlinmud.action.impl.item

import kotlinmud.action.exception.InvokeException
import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createGetFromContainerMessage
import kotlinmud.io.factory.itemInInventoryAndAvailableInventory
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item

fun createGetFromItemAction(): Action {
    return Action(
        Command.GET,
        mustBeAwake(),
        itemInInventoryAndAvailableInventory(),
        listOf(0, 2, 1)
    ) {
        val item = it.get<Item>(Syntax.ITEM_IN_AVAILABLE_INVENTORY)
        val itemWithInventory = it.get<Item>(Syntax.AVAILABLE_ITEM_INVENTORY)

        try {
            it.giveItemToMob(item, it.getMob())
        } catch (e: InvokeException) {
            return@Action it.createErrorResponse(e.toMessage())
        }

        it.createOkResponse(createGetFromContainerMessage(it.getMob(), itemWithInventory, item))
    }
}
