package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.directionToExit
import kotlinmud.io.model.EmptyResponse
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinx.coroutines.runBlocking

private fun move(command: Command, direction: Direction): Action {
    return Action(
        command,
        mustBeStanding(),
        directionToExit(),
        listOf(0),
        listOf(Cost(CostType.MV_AMOUNT, 1)),
        Command.LOOK
    ) {
        val destination = it.get<Room>(Syntax.DIRECTION_TO_EXIT)
        runBlocking { it.moveMob(destination, direction) }
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
