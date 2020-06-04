package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAwake
import kotlinmud.action.type.Command
import kotlinmud.io.factory.itemInInventory
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createDropAction(): Action {
    return Action(Command.DROP, mustBeAwake(), itemInInventory()) {
        val item = it.get<Item>(Syntax.ITEM_IN_INVENTORY)
        it.changeItemOwner(item, it.getRoom())
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you drop ${item.name}.")
                .toObservers("${it.getMob()} drops ${item.name}.")
                .build()
        )
    }
}
