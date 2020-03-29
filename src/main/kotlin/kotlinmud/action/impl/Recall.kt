package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.EmptyResponse
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createRecallAction(): Action {
    return Action(
        Command.RECALL,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            svc.putMobInRoom(request.mob, svc.getRecall())
            EmptyResponse()
        },
        Command.LOOK
    )
}
