package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createOpenMessage
import kotlinmud.io.factory.doorInRoom
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Door
import kotlinmud.room.type.DoorDisposition

fun createOpenAction(): Action {
    return Action(Command.OPEN, mustBeAlert(), doorInRoom()) {
        val door = it.get<Door>(Syntax.DOOR_IN_ROOM)
        when (door.disposition) {
            DoorDisposition.LOCKED -> it.createErrorResponse(
                messageToActionCreator("it is locked.")
            )
            DoorDisposition.OPEN -> it.createErrorResponse(
                messageToActionCreator("it is already open.")
            )
            DoorDisposition.CLOSED -> {
                door.disposition = DoorDisposition.OPEN
                it.createOkResponse(createOpenMessage(it.getMob(), door))
            }
        }
    }
}
