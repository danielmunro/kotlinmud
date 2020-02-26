package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createFleeAction(): Action {
    return Action(
        Command.FLEE,
        mustBeStanding(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            if (request.mob.disposition != Disposition.FIGHTING) {
                return@Action svc.createResponse(Message("you are not fighting anyone."))
            }
            svc.endFightFor(request.mob)
            svc.moveMob(
                request.mob,
                request.room.exits.random().destination)
            svc.createResponse(
                Message(
                    "you flee!",
                    "${request.mob.name} flees!")
            )
        },
        Command.LOOK)
}
