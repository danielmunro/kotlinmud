package kotlinmud.action

import kotlinmud.EventService
import kotlinmud.event.MobMoveEvent
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.room.Direction

fun createNorthAction(): Action {
    return Action(
        Command.NORTH,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.DIRECTION_TO_EXIT),
        fun (eventService: EventService, contextCollection: ContextCollection, request: Request): Response {
            eventService.publish(MobMoveEvent(
                request.mob,
                contextCollection.getResultBySyntax(Syntax.DIRECTION_TO_EXIT)!!,
                Direction.NORTH))
            return Response(request, "you move north.")
        })
}
