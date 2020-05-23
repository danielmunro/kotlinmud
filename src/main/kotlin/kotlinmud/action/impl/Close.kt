package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.doorInRoom
import kotlinmud.io.messageToActionCreator
import kotlinmud.world.room.exit.Door
import kotlinmud.world.room.exit.DoorDisposition

fun createCloseAction(): Action {
    return Action(Command.CLOSE, mustBeAlert(), doorInRoom()) {
        val door: Door = it.get(Syntax.DOOR_IN_ROOM)
        if (door.disposition != DoorDisposition.OPEN) {
            return@Action it.createErrorResponse(messageToActionCreator("it is already closed."))
        }
        door.disposition = DoorDisposition.CLOSED
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you close $door.")
                .toObservers("${it.getMob()} closes $door.")
                .build()
        )
    }
}
