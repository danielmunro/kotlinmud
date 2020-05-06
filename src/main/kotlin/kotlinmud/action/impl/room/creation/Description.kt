package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.subcommandWithFreeForm

fun createRoomDescriptionAction(): Action {
    return Action(Command.ROOM_DESCRIPTION, mustBeAlert(), subcommandWithFreeForm()) {
        val newRoom = it.getNewRoom() ?: it.createNewRoom("a new room")
        val description = it.get<String>(Syntax.FREE_FORM)
        newRoom.roomBuilder.description(description)
        it.createResponse(Message("the room builder description is now $description."))
    }
}
