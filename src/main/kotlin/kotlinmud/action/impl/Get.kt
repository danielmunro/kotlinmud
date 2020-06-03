package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAwake
import kotlinmud.action.type.Command
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.factory.itemInRoom
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createGetAction(): Action {
    return Action(Command.GET, mustBeAwake(), itemInRoom()) {
        val item = it.get<Item>(Syntax.ITEM_IN_ROOM)
        if (!item.canOwn) {
            return@Action it.createOkResponse(
                MessageBuilder()
                    .toActionCreator("you cannot pick that up.")
                    .build()
            )
        }
        it.changeItemOwner(item, it.getMob())
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you pick up ${item.name}.")
                .toObservers("${it.getMob()} picks up ${item.name}.")
                .build()
        )
    }
}
