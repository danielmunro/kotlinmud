package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createSayAction(): Action {
    return Action(
        Command.SAY,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            svc.createResponse(
                Message("you sit down.", "${request.mob.name} sits down.")
            )
        })
}
