package kotlinmud.action.impl.room

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax

fun createRoomBuildAction(): Action {
    return Action(
        Command.ROOM_BUILD,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND),
        { svc: ActionContextService, request: Request ->
            val room = svc.buildRoom(request.mob)
            val exit = request.room.exits.find { it.destination == room }!!
            svc.createResponse(Message("you have built $room to the ${exit.direction.value}."))
        }
    )
}
