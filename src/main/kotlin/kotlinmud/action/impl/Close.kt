package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createCloseMessage
import kotlinmud.io.factory.doorInRoom
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.type.DoorDisposition
import org.jetbrains.exposed.sql.transactions.transaction

fun createCloseAction(): Action {
    return Action(Command.CLOSE, mustBeAlert(), doorInRoom()) {
        val door = it.get<DoorDAO>(Syntax.DOOR_IN_ROOM)
        if (door.disposition != DoorDisposition.OPEN) {
            return@Action it.createErrorResponse(messageToActionCreator("it is already closed."))
        }
        transaction { door.disposition = DoorDisposition.CLOSED }
        it.createOkResponse(createCloseMessage(it.getMob(), door))
    }
}
