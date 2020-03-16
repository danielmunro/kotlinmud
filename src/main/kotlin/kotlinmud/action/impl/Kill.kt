package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

fun createKillAction(): Action {
    return Action(
        Command.KILL,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.MOB_IN_ROOM),
        { svc: ActionContextService, request: Request ->
            val target = svc.get<Mob>(Syntax.MOB_IN_ROOM)
            svc.createFightFor(request.mob)
            svc.createResponse(Message(
                    "you scream and attack ${target.name}!",
                    "${request.mob.name} screams and attacks you!",
                    "${request.mob.name} screams and attacks ${target.name}!"))
        })
}
