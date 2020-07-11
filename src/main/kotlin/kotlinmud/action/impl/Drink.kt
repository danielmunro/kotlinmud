package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.drink
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.helper.applyAffectFromItem

fun createDrinkAction(): Action {
    return Action(Command.DRINK, mustBeAwake(), drink()) {
        val item = it.get<ItemDAO>(Syntax.AVAILABLE_DRINK)
        val appetite = it.getMobCard().appetite

        if (appetite.isFull()) {
            return@Action it.createErrorResponse(messageToActionCreator("you are full."))
        }

        item.quantity?.let { quantity ->
            item.quantity = quantity - 1
        }

        appetite.nourishThirst()
        applyAffectFromItem(it.getMob(), item)

        val empty = if (item.quantity == 0) " $item is now empty." else ""

        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you drink ${item.drink.toString().toLowerCase()} from $item.$empty")
                .toObservers("you drink ${item.drink.toString().toLowerCase()} from $item.")
                .build()
        )
    }
}
