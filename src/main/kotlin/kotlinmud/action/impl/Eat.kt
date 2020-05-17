package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.IOStatus
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.foodInInventory
import kotlinmud.io.messageToActionCreator
import kotlinmud.item.Item

fun createEatAction(): Action {
    return Action(Command.EAT, mustBeAwake(), foodInInventory()) {
        val item = it.get<Item>(Syntax.AVAILABLE_FOOD)

        if (it.getMob().appetite.isFull()) {
            return@Action it.createResponse(messageToActionCreator("you are full."), IOStatus.ERROR)
        }

        it.getMob().appetite.nourishHunger(item.quantity)
        it.getMob().affects().copyFrom(item)
        it.destroy(item)

        it.createResponse(
            MessageBuilder()
                .toActionCreator("you eat $item.")
                .toObservers("${it.getMob()} eats $item.")
                .build()
        )
    }
}
