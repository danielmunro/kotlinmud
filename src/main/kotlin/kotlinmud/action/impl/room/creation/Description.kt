package kotlinmud.action.impl.room.creation

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithFreeForm
import kotlinmud.io.type.Syntax

fun createRoomDescriptionAction(): Action {
    return Action(
        Command.ROOM_DESCRIPTION,
        mustBeAlert(),
        subcommandWithFreeForm()
    ) {
        val newRoom = it.getNewRoom() ?: it.createNewRoom("a new room")
        val description = it.get<String>(Syntax.FREE_FORM)
        newRoom.roomBuilder.description(description)
        it.createOkResponse(
            messageToActionCreator("the room builder description is now $description.")
        )
    }
}
