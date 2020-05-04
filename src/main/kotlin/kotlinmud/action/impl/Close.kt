package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.IOStatus
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.world.room.exit.Door
import kotlinmud.world.room.exit.DoorDisposition

fun createCloseAction(): Action {
    return Action(
        Command.CLOSE,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.DOOR_IN_ROOM)) {
            val door: Door = it.get(Syntax.DOOR_IN_ROOM)
            if (door.disposition != DoorDisposition.OPEN) {
                return@Action it.createResponse(Message("it is already closed."), IOStatus.ERROR)
            }
            door.disposition = DoorDisposition.CLOSED
            it.createResponse(
                Message("you close $door.", "${it.getMob()} closes $door.")
            )
        }
}
