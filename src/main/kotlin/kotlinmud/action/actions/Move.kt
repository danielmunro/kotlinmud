package kotlinmud.action.actions

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.event.EventResponse
import kotlinmud.event.createMobMoveEvent
import kotlinmud.event.event.MobMoveEvent
import kotlinmud.io.Message
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
        fun (actionContextService: ActionContextService, actionContextList: ActionContextList, request: Request): Response {
            val eventResponse: EventResponse<MobMoveEvent> = actionContextService.publishEvent(
                createMobMoveEvent(request.mob, actionContextList.getResultBySyntax(Syntax.COMMAND), direction)
            )
            return Response(
                request,
                Message(
                    "you move ${direction.value}.",
                    "${request.mob.name} moves ${direction.value}."))
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
