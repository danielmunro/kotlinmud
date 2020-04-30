package kotlinmud.action.impl.room

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.world.room.Direction

fun createRoomNewAction(): Action {
    return Action(
        Command.ROOM_NEW,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.DIRECTION_WITH_NO_EXIT, Syntax.FREE_FORM),
        { svc: ActionContextService, request: Request ->
            val direction = svc.get<Direction>(Syntax.DIRECTION_WITH_NO_EXIT)
            val name = svc.get<String>(Syntax.FREE_FORM)
            svc.createRoomBuilder(request.mob, request.room, name, direction)
            svc.createResponse(Message("you are starting to create a room to the ${direction.value}. Please finalize your room and then type 'room build' while standing in this room."))
        }
    )
}
