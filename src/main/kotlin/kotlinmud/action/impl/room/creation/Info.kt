package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.subcommand

fun createRoomInfoAction(): Action {
    return Action(Command.ROOM_INFO, mustBeAlert(), subcommand()) {
        val newRoom = it.getNewRoom() ?: return@Action it.createResponse(Message("You need to start creating a room first. Try 'room new'."))
        it.createResponse(Message("your room: ${newRoom.roomBuilder}"))
    }
}
