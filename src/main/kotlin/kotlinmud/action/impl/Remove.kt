package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.equippedItem
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO

fun createRemoveAction(): Action {
    return Action(Command.REMOVE, mustBeAlert(), equippedItem()) {
        val item = it.get<ItemDAO>(Syntax.EQUIPPED_ITEM)
        it.getMob().equipped.minus(item)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you remove $item and put it in your inventory.")
                .toObservers("${it.getMob()} removes $item and puts it in their inventory.")
                .build()
        )
    }
}
