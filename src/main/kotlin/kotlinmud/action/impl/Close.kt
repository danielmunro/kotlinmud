package kotlinmud.action.impl

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createCloseMessage
import kotlinmud.io.factory.doorInRoom
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Door
import kotlinmud.room.type.DoorDisposition

fun createCloseAction(): Action {
    return ActionBuilder(Command.CLOSE).also {
        it.syntax = doorInRoom()
    } build {
        val door = it.get<Door>(Syntax.DOOR_IN_ROOM)
        if (door.disposition != DoorDisposition.OPEN) {
            return@build it.createErrorResponse(messageToActionCreator("it is already closed."))
        }
        door.disposition = DoorDisposition.CLOSED
        it.createOkResponse(createCloseMessage(it.getMob(), door))
    }
}
