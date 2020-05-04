package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.world.room.Direction

fun createRoomBuildAction(): Action {
    return Action(
        Command.ROOM_BUILD,
        mustBeAlert(),
        listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.DIRECTION_WITH_NO_EXIT),
        { svc ->
            val direction = svc.get<Direction>(Syntax.DIRECTION_WITH_NO_EXIT)
            val room = svc.buildRoom(svc.getMob(), direction)
            val exit = svc.getRoom().exits.find { it.destination == room }!!
            svc.createResponse(Message("you have built ${room.name} to the ${exit.direction.value}."))
        }
    )
}
