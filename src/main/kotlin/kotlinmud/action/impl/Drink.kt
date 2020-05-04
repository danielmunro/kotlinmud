package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.IOStatus
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createDrinkAction(): Action {
    return Action(
        Command.DRINK,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.AVAILABLE_DRINK),
        {
            val item = it.get<Item>(Syntax.AVAILABLE_DRINK)

            if (it.getMob().appetite.isFull()) {
                return@Action it.createResponse(Message("you are full."), IOStatus.ERROR)
            }

            if (item.quantity > 0) {
                item.quantity--
            }

            it.getMob().appetite.nourishThirst()
            it.getMob().affects().copyFrom(item)

            val empty = if (item.quantity == 0) " $item is now empty." else ""

            it.createResponse(
                Message("you drink ${item.drink.toString().toLowerCase()} from $item.$empty")
            )
        })
}
