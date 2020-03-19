package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
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
            TODO()
        })
}
