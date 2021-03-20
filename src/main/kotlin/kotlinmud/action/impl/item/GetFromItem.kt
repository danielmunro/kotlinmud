package kotlinmud.action.impl.item

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.exception.InvokeException
import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createGetFromContainerMessage
import kotlinmud.io.factory.itemInInventoryAndAvailableInventory
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createGetFromItemAction(): Action {
    return ActionBuilder(Command.GET).also {
        it.dispositions = mustBeAwake()
        it.syntax = itemInInventoryAndAvailableInventory()
        it.argumentOrder = listOf(0, 2, 1)
    } build {
        val item = it.get<Item>(Syntax.ITEM_IN_AVAILABLE_INVENTORY)
        val itemWithInventory = it.get<Item>(Syntax.AVAILABLE_ITEM_INVENTORY)

        try {
            it.giveItemToMob(item, it.getMob())
        } catch (e: InvokeException) {
            return@build it.createErrorResponse(e.toMessage())
        }

        it.createOkResponse(createGetFromContainerMessage(it.getMob(), itemWithInventory, item))
    }
}
