package kotlinmud.action.impl

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createFleeAction(): Action {
    return Action(
        Command.FLEE,
        mustBeAlert(),
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
