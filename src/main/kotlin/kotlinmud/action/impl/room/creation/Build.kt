package kotlinmud.action.impl.room.creation

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.factory.subcommandDirectionNoExit
import kotlinmud.io.type.Syntax
import kotlinmud.room.type.Direction

fun createRoomBuildAction(): Action {
    return Action(
        Command.ROOM_BUILD,
        mustBeAlert(),
        subcommandDirectionNoExit()
    ) { svc ->
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
