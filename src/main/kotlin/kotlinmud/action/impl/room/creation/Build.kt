package kotlinmud.action.impl.room.creation

import kotlinmud.action.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.subcommandDirectionNoExit
import kotlinmud.world.room.Direction

fun createRoomBuildAction(): Action {
    return Action(Command.ROOM_BUILD, mustBeAlert(), subcommandDirectionNoExit()) { svc ->
        val direction = svc.get<Direction>(Syntax.DIRECTION_WITH_NO_EXIT)
        val room = svc.buildRoom(svc.getMob(), direction)
        val exit = svc.getExits().find { it.destination == room }!!
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator("you have built ${room.name} ${exit.direction.value}.")
                .toObservers("${svc.getMob()} builds ${room.name} ${exit.direction.value}.")
                .build()
        )
    }
}
