package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.EmptyResponse
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType

fun createRecallAction(): Action {
    return Action(
        Command.RECALL,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            svc.putMobInRoom(request.mob, svc.getRecall())
            EmptyResponse()
        },
        listOf(
            Cost(CostType.MV_PERCENT, 50)
        ),
        Command.LOOK
    )
}
