package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.world.room.Direction

fun createRoomBuildAction(): Action {
    return Action(
        Command.ROOM_BUILD,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.DIRECTION_WITH_NO_EXIT),
        { svc: ActionContextService, request: Request ->
            val direction = svc.get<Direction>(Syntax.DIRECTION_WITH_NO_EXIT)
            val room = svc.buildRoom(request.mob, direction)
            val exit = request.room.exits.find { it.destination == room }!!
            svc.createResponse(Message("you have built ${room.name} to the ${exit.direction.value}."))
        }
    )
}
