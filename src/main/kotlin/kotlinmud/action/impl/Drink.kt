package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.IOStatus
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.item.Item

fun createDrinkAction(): Action {
    return Action(
        Command.DRINK,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.AVAILABLE_DRINK),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<Item>(Syntax.AVAILABLE_DRINK)

            if (request.mob.appetite.isFull()) {
                return@Action svc.createResponse(Message("you are full."), IOStatus.ERROR)
            }

            if (item.quantity > 0) {
                item.quantity--
            }

            request.mob.appetite.nourishThirst()

            item.affects.forEach {
                request.mob.affects.add(it.copy())
            }

            val empty = if (item.quantity == 0) " $item is now empty." else ""

            svc.createResponse(
                Message("you drink ${item.drink.toString().toLowerCase()} from $item.$empty")
            )
        })
}
