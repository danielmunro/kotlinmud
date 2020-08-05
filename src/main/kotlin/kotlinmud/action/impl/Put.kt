package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.availableInventoryAndItem
import kotlinmud.io.factory.createPutMessage
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO

fun createPutAction(): Action {
    return Action(Command.PUT, mustBeAwake(), availableInventoryAndItem()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_IN_INVENTORY)
        val container = it.get<ItemDAO>(Syntax.AVAILABLE_ITEM_INVENTORY)
        try {
            it.putItemInContainer(item, container)
        } catch (e: Exception) {
            return@Action it.createErrorResponse(MessageBuilder().toActionCreator(e.message!!).build())
        }
        it.createOkResponse(createPutMessage(it.getMob(), item, container))
    }
}
