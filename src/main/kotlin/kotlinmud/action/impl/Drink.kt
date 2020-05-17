package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.IOStatus
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.drink
import kotlinmud.io.messageToActionCreator
import kotlinmud.item.Item

fun createDrinkAction(): Action {
    return Action(Command.DRINK, mustBeAwake(), drink()) {
        val item = it.get<Item>(Syntax.AVAILABLE_DRINK)

        if (it.getMob().appetite.isFull()) {
            return@Action it.createResponse(
                messageToActionCreator("you are full."),
                IOStatus.ERROR
            )
        }

        if (item.quantity > 0) {
            item.quantity--
        }

        it.getMob().appetite.nourishThirst()
        it.getMob().affects().copyFrom(item)

        val empty = if (item.quantity == 0) " $item is now empty." else ""

        it.createResponse(
            MessageBuilder()
                .toActionCreator("you drink ${item.drink.toString().toLowerCase()} from $item.$empty")
                .toObservers("you drink ${item.drink.toString().toLowerCase()} from $item.")
                .build()
        )
    }
}
