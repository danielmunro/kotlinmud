package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createWakeAction(): Action {
    return Action(
        Command.WAKE,
        listOf(Disposition.SLEEPING, Disposition.SITTING),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            request.mob.disposition = Disposition.STANDING
            svc.createResponse(
                Message("you stand up.", "${request.mob.name} stands up."))
        })
}
