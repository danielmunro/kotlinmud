package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax

fun createRoomNewAction(): Action {
    return Action(
        Command.ROOM_NEW,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.FREE_FORM)) {
            val name = it.get<String>(Syntax.FREE_FORM)
            it.createRoomBuilder(it.getMob(), it.getRoom(), name)
            it.createResponse(Message("you are starting to create a room. Finalize your room and then type 'room build <direction>'."))
        }
}
