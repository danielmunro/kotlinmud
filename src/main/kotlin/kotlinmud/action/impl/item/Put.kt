package kotlinmud.action.impl.item

import kotlinmud.action.exception.InvokeException
import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.availableInventoryAndItem
import kotlinmud.io.factory.createPutMessage
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createPutAction(): Action {
    return Action(Command.PUT, mustBeAwake(), availableInventoryAndItem()) {
        val item = it.get<Item>(Syntax.ITEM_IN_INVENTORY)
        val container = it.get<Item>(Syntax.AVAILABLE_ITEM_INVENTORY)
        val mob = it.getMob()
        mob.items.remove(item)

        try {
            it.putItemInContainer(item, container)
        } catch (e: InvokeException) {
            return@Action it.createErrorResponse(e.toMessage())
        }

        it.createOkResponse(createPutMessage(mob, item, container))
    }
}
