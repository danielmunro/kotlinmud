package kotlinmud.action.actions

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.MobEntity

fun createKillAction(): Action {
    return Action(
        Command.KILL,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.MOB_IN_ROOM),
        { svc: ActionContextService, request: Request ->
            val target = svc.get<MobEntity>(Syntax.MOB_IN_ROOM)
            svc.createResponse(Message(
                    "you scream and attack ${target.name}!",
                    "${request.mob.name} screams and attacks you!",
                    "${request.mob.name} screams and attacks ${target.name}!"))
        })
}
