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

fun createEatAction(): Action {
    return Action(
        Command.EAT,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.AVAILABLE_FOOD),
        { svc: ActionContextService, request: Request ->
            val item = svc.get<Item>(Syntax.AVAILABLE_FOOD)

            if (request.mob.appetite.isFull()) {
                return@Action svc.createResponse(Message("you are full."), IOStatus.ERROR)
            }

            request.mob.appetite.nourishHunger(item.quantity)
            request.mob.inventory.items.remove(item)
            item.affects.forEach {
                request.mob.affects.add(it.copy())
            }

            svc.createResponse(
                Message("you eat $item.")
            )
        })
}
