package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createHealListAction(): Action {
    return Action(
        Command.HEAL,
        mustBeStanding(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, _: Request ->
            svc.createResponse(
                Message(
                    "success, healer"
                )
            )
        })
}
