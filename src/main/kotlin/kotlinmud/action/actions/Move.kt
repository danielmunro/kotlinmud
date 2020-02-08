package kotlinmud.action.actions

import kotlinmud.EventService
import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.ContextCollection
import kotlinmud.event.MobMoveEvent
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.room.Direction

private fun move(command: Command, direction: Direction): Action {
    return Action(
        command,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.DIRECTION_TO_EXIT),
        fun (eventService: EventService, contextCollection: ContextCollection, request: Request): Response {
            eventService.publish(
                MobMoveEvent(
                    request.mob,
                    contextCollection.getResultBySyntax(Syntax.DIRECTION_TO_EXIT)!!,
                    direction)
            )
            return Response(request, "you move ${direction.name.toLowerCase()}.")
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
