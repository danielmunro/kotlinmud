package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.itemInInventory
import kotlinmud.item.Item

fun createDropAction(): Action {
    return Action(Command.DROP, mustBeAwake(), itemInInventory()) {
        val item = it.get<Item>(Syntax.ITEM_IN_INVENTORY)
        it.changeItemOwner(item, it.getRoom())
        it.createResponse(
            MessageBuilder()
                .toActionCreator("you drop ${item.name}.")
                .toObservers("${it.getMob()} drops ${item.name}.")
                .build()
        )
    }
}
