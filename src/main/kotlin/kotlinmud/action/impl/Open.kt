package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.room.exit.Door
import kotlinmud.room.exit.DoorDisposition

fun createOpenAction(): Action {
    return Action(
        Command.OPEN,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.DOOR_IN_ROOM),
        { svc: ActionContextService, request: Request ->
            val door: Door = svc.get(Syntax.DOOR_IN_ROOM)
            door.disposition = DoorDisposition.OPEN
            svc.createResponse(
                Message("you open $door.", "${request.mob.name} opens $door.")
            )
        })
}
