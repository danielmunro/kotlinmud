package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.itemInInventory
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO

fun createDropAction(): Action {
    return Action(Command.DROP, mustBeAwake(), itemInInventory()) {
        val item = it.get<ItemDAO>(Syntax.ITEM_IN_INVENTORY)
        it.putItemInRoom(item, it.getRoom())
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you drop ${item.name}.")
                .toObservers("${it.getMob()} drops ${item.name}.")
                .build()
        )
    }
}
