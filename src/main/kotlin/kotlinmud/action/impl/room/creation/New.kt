package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createRoomNewAction(): Action {
    return Action(
        Command.ROOM_NEW,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            val name = svc.get<String>(Syntax.FREE_FORM)
            svc.createRoomBuilder(request.mob, request.room, name)
            svc.createResponse(Message("you are starting to create a room. Finalize your room and then type 'room build <direction>'."))
        }
    )
}
