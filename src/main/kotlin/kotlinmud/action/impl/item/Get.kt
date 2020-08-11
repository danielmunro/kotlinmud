package kotlinmud.action.impl.item

import kotlinmud.action.exception.InvokeException
import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createGetMessage
import kotlinmud.io.factory.itemInRoom
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO

fun createGetAction(): Action {
    return Action(Command.GET, mustBeAwake(), itemInRoom()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_IN_ROOM)

        if (!item.canOwn) {
            return@Action it.createOkResponse(messageToActionCreator("you cannot pick that up."))
        }

        try {
            it.giveItemToMob(item, it.getMob())
        } catch (e: InvokeException) {
            return@Action it.createErrorResponse(e.toMessage())
        }

        it.createOkResponse(createGetMessage(it.getMob(), item))
    }
}
