package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAwake
import kotlinmud.action.type.Command
import kotlinmud.io.factory.drink
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item

fun createDrinkAction(): Action {
    return Action(Command.DRINK, mustBeAwake(), drink()) {
        val item = it.get<Item>(Syntax.AVAILABLE_DRINK)
        val appetite = it.getMobCard().appetite

        if (appetite.isFull()) {
            return@Action it.createErrorResponse(messageToActionCreator("you are full."))
        }

        if (item.quantity > 0) {
            item.quantity--
        }

        appetite.nourishThirst()
        it.getMob().affects().copyFrom(item)

        val empty = if (item.quantity == 0) " $item is now empty." else ""

        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you drink ${item.drink.toString().toLowerCase()} from $item.$empty")
                .toObservers("you drink ${item.drink.toString().toLowerCase()} from $item.")
                .build()
        )
    }
}
