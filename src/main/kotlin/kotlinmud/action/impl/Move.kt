package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.directionToExit
import kotlinmud.io.model.EmptyResponse
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction

private fun move(command: Command, direction: Direction): Action {
    return Action(
        command,
        mustBeStanding(),
        directionToExit(),
        listOf(Cost(CostType.MV_AMOUNT, 1)),
        Command.LOOK
    ) {
        val room = it.getRoom()
        val destination = it.get<Room>(Syntax.DIRECTION_TO_EXIT)
        it.moveMob(destination, direction)
        val elevationChange = room.elevation - destination.elevation
        if (elevationChange > MAX_WALKABLE_ELEVATION) {
            takeDamageFromFall(it.getMob(), elevationChange)
        }
        EmptyResponse()
    }
}

fun takeDamageFromFall(mob: Mob, elevationChange: Int) {
    val damage = when {
        elevationChange < MAX_WALKABLE_ELEVATION + 2 -> 3
        elevationChange < MAX_WALKABLE_ELEVATION + 5 -> 10
        elevationChange < MAX_WALKABLE_ELEVATION + 10 -> 50
        else -> elevationChange * 10
    }
    mob.hp -= damage
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
