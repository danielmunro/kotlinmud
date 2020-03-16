package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createSitAction(): Action {
    return Action(
        Command.SIT,
        listOf(Disposition.SLEEPING, Disposition.STANDING),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            request.mob.disposition = Disposition.SITTING
            svc.createResponse(
                Message("you sit down.", "${request.mob.name} sits down."))
        })
}
