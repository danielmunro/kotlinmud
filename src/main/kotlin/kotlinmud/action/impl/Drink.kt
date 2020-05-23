package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.mustBeAwake
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.drink
import kotlinmud.io.messageToActionCreator
import kotlinmud.item.Item

fun createDrinkAction(): Action {
    return Action(Command.DRINK, mustBeAwake(), drink()) {
        val item = it.get<Item>(Syntax.AVAILABLE_DRINK)

        if (it.getMob().appetite.isFull()) {
            return@Action it.createErrorResponse(messageToActionCreator("you are full."))
        }

        if (item.quantity > 0) {
            item.quantity--
        }

        it.getMob().appetite.nourishThirst()
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
