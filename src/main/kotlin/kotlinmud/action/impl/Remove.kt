package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.equippedItem
import kotlinmud.item.Item

fun createRemoveAction(): Action {
    return Action(Command.REMOVE, mustBeAlert(), equippedItem()) {
        val item = it.get<Item>(Syntax.EQUIPPED_ITEM)
        it.getMob().equipped.remove(item)
        it.createResponse(
            MessageBuilder()
                .toActionCreator("you remove $item and put it in your inventory.")
                .toObservers("${it.getMob()} removes $item and puts it in their inventory.")
                .build()
        )
    }
}
