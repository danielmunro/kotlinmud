package kotlinmud.action.impl.room.creation

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommand

fun createRoomInfoAction(): Action {
    return Action(Command.ROOM_INFO, mustBeAlert(), subcommand()) {
        val newRoom = it.getNewRoom()
            ?: return@Action it.createOkResponse(messageToActionCreator("You need to start creating a room first. Try 'room new'."))
        it.createOkResponse(messageToActionCreator("your room: ${newRoom.room.name}"))
    }
}
