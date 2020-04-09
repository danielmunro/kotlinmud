package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.IOStatus
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.world.room.exit.Door
import kotlinmud.world.room.exit.DoorDisposition

fun createCloseAction(): Action {
    return Action(
        Command.CLOSE,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.DOOR_IN_ROOM),
        { svc: ActionContextService, request: Request ->
            val door: Door = svc.get(Syntax.DOOR_IN_ROOM)
            if (door.disposition != DoorDisposition.OPEN) {
                return@Action svc.createResponse(Message("it is already closed."), IOStatus.ERROR)
            }
            door.disposition = DoorDisposition.CLOSED
            svc.createResponse(
                Message("you close $door.", "${request.mob.name} closes $door.")
            )
        })
}
