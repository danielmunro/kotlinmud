package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.itemInRoom
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO

fun createGetAction(): Action {
    return Action(Command.GET, mustBeAwake(), itemInRoom()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_IN_ROOM)
        if (!item.canOwn) {
            return@Action it.createOkResponse(
                MessageBuilder()
                    .toActionCreator("you cannot pick that up.")
                    .build()
            )
        }
        try {
            it.giveItemToMob(item, it.getMob())
        } catch (e: Exception) {
            return@Action it.createErrorResponse(MessageBuilder().toActionCreator(e.message!!).build())
        }

        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you pick up ${item.name}.")
                .toObservers("${it.getMob()} picks up ${item.name}.")
                .build()
        )
    }
}
