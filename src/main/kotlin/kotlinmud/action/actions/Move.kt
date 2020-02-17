package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.io.EmptyResponse
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.mob.MobEntity
import kotlinmud.room.Direction
import kotlinmud.room.RoomEntity

private fun move(command: Command, direction: Direction): Action {
    return Action(
        command,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.DIRECTION_TO_EXIT),
        { actionContextService: ActionContextService, actionContextList: ActionContextList, request: Request ->
            val destination = actionContextList.getResultBySyntax<RoomEntity>(Syntax.DIRECTION_TO_EXIT)
            actionContextService.sendMessageToRoom(createLeaveMessage(request.mob, direction), request.room, request.mob)
            actionContextService.moveMob(request.mob, destination)
            actionContextService.sendMessageToRoom(createArriveMessage(request.mob), destination, request.mob)
            EmptyResponse()
        },
        Command.LOOK)
}

fun createLeaveMessage(mob: MobEntity, direction: Direction): Message {
    return Message(
        "you leave heading ${direction.value}.",
        "${mob.name} leaves heading ${direction.value}.")
}

fun createArriveMessage(mob: MobEntity): Message {
    return Message(
        "",
        "${mob.name} arrives.")
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
