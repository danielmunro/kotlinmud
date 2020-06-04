package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.factory.doorInRoom
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Door
import kotlinmud.room.type.DoorDisposition

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
