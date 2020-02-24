package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createSleepAction(): Action {
    return Action(
        Command.SLEEP,
        listOf(Disposition.STANDING, Disposition.SITTING),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            request.mob.disposition = Disposition.SLEEPING
            svc.createResponse(
                Message("you lay down and go to sleep.", "${request.mob.name} lays down and goes to sleep.")
            )
        })
}
