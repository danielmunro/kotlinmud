package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity

private fun move(command: Command, direction: Direction): Action {
    return Action(
        command,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.DIRECTION_TO_EXIT),
        { actionContextService: ActionContextService, actionContextList: ActionContextList, request: Request ->
            val destination = actionContextList.getResultBySyntax<RoomEntity>(Syntax.DIRECTION_TO_EXIT)
            actionContextService.sendMessageToRoom(
                Message(
                    "you leave heading ${direction.value}.",
                    "${request.mob.name} leaves heading ${direction.value}."),
                request.room,
                request.mob)
            actionContextService.moveMob(request.mob, destination)
            Response(
                request,
                actionContextList,
                Message(
                    "",
                    "${request.mob.name} arrives."))
        },
        Command.LOOK)
}

fun createNorthAction(): Action {
    return move(Command.NORTH, Direction.NORTH)
}

fun createSouthAction(): Action {
    return move(Command.SOUTH, Direction.SOUTH)
}

fun createEastAction(): Action {
    return move(Command.EAST, Direction.EAST)
}

fun createWestAction(): Action {
    return move(Command.WEST, Direction.WEST)
}

fun createUpAction(): Action {
    return move(Command.UP, Direction.UP)
}

fun createDownAction(): Action {
    return move(Command.DOWN, Direction.DOWN)
}
