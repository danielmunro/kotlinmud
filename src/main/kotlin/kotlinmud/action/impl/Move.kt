package kotlinmud.action.impl

import kotlinmud.action.*
import kotlinmud.io.EmptyResponse
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob
import kotlinmud.room.Direction
import kotlinmud.room.Room

private fun move(command: Command, direction: Direction): Action {
    return Action(
        command,
        mustBeStanding(),
        listOf(Syntax.DIRECTION_TO_EXIT),
        { svc: ActionContextService, request: Request ->
            val destination = svc.get<Room>(Syntax.DIRECTION_TO_EXIT)
            svc.sendMessageToRoom(createLeaveMessage(request.mob, direction), request.room, request.mob)
            svc.moveMob(request.mob, destination)
            svc.sendMessageToRoom(createArriveMessage(request.mob), destination, request.mob)
            EmptyResponse()
        },
        Command.LOOK)
}

fun createLeaveMessage(mob: Mob, direction: Direction): Message {
    return Message(
        "you leave heading ${direction.value}.",
        "${mob.name} leaves heading ${direction.value}.")
}

fun createArriveMessage(mob: Mob): Message {
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
