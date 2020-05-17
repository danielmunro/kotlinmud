package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Syntax
import kotlinmud.io.messageToActionCreator
import kotlinmud.io.subcommandWithFreeForm

fun createRoomNewAction(): Action {
    return Action(Command.ROOM_NEW, mustBeAlert(), subcommandWithFreeForm()) {
        val name = it.get<String>(Syntax.FREE_FORM)
        it.createNewRoom(name)
        it.createResponse(
            messageToActionCreator("you are starting to create a room. Finalize your room and then type 'room build <direction>'.")
        )
    }
}
