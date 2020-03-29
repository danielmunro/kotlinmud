package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.EmptyResponse
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType
import kotlinmud.room.Direction
import kotlinmud.room.Room

private fun move(command: Command, direction: Direction): Action {
    return Action(
        command,
        mustBeStanding(),
        listOf(Syntax.DIRECTION_TO_EXIT),
        { svc: ActionContextService, request: Request ->
            val destination = svc.get<Room>(Syntax.DIRECTION_TO_EXIT)
            svc.moveMob(request.mob, destination, direction)
            EmptyResponse()
        },
        listOf(
            Cost(CostType.MV_AMOUNT, 1)
        ),
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
