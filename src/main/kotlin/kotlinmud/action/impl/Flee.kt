package kotlinmud.action.impl

import kotlinmud.action.*
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createFleeAction(): Action {
    return Action(
        Command.FLEE,
        mustBeFighting(),
        listOf(Syntax.COMMAND),
        { svc: ActionContextService, request: Request ->
            svc.endFightFor(request.mob)
            val exit = request.room.exits.random()
            svc.moveMob(
                request.mob,
                exit.destination,
                exit.direction)
            svc.createResponse(
                Message(
                    "you flee!",
                    "${request.mob.name} flees!")
            )
        },
        Command.LOOK)
}
