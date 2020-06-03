package kotlinmud.action.impl.room.creation

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithFreeForm
import kotlinmud.io.type.Syntax

fun createRoomNewAction(): Action {
    return Action(Command.ROOM_NEW, mustBeAlert(), subcommandWithFreeForm()) {
        val name = it.get<String>(Syntax.FREE_FORM)
        it.createNewRoom(name)
        it.createOkResponse(
            messageToActionCreator("you are starting to create a room. Finalize your room and then type 'room build <direction>'.")
        )
    }
}
