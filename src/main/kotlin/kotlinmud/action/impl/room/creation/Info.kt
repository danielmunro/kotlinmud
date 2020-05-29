package kotlinmud.action.impl.room.creation

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.messageToActionCreator
import kotlinmud.io.subcommand

fun createRoomInfoAction(): Action {
    return Action(Command.ROOM_INFO, mustBeAlert(), subcommand()) {
        val newRoom = it.getNewRoom()
            ?: return@Action it.createOkResponse(messageToActionCreator("You need to start creating a room first. Try 'room new'."))
        it.createOkResponse(messageToActionCreator("your room: ${newRoom.roomBuilder}"))
    }
}
