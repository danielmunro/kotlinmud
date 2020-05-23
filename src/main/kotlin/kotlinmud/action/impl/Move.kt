package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.mustBeStanding
import kotlinmud.action.type.Command
import kotlinmud.io.EmptyResponse
import kotlinmud.io.Syntax
import kotlinmud.io.directionToExit
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType
import kotlinmud.world.room.Direction
import kotlinmud.world.room.Room

private fun move(command: Command, direction: Direction): Action {
    return Action(command, mustBeStanding(), directionToExit(), listOf(Cost(CostType.MV_AMOUNT, 1)), Command.LOOK) {
        val destination = it.get<Room>(Syntax.DIRECTION_TO_EXIT)
        it.moveMob(destination, direction)
        EmptyResponse()
    }
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
