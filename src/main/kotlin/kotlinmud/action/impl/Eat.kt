package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.foodInInventory
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.helper.applyAffectFromItem

fun createEatAction(): Action {
    return Action(Command.EAT, mustBeAwake(), foodInInventory()) {
        val item = it.get<ItemDAO>(Syntax.AVAILABLE_FOOD)
        val appetite = it.getMobCard().appetite

        if (appetite.isFull()) {
            return@Action it.createErrorResponse(messageToActionCreator("you are full."))
        }

        appetite.nourishHunger(item.quantity!!)
        applyAffectFromItem(it.getMob(), item)
        it.destroy(item)

        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you eat $item.")
                .toObservers("${it.getMob()} eats $item.")
                .build()
        )
    }
}
