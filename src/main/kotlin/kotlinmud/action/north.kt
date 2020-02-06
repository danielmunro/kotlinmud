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
        arrayOf(Syntax.COMMAND),
        fun (eventService: EventService, buf: Request): Response {
            eventService.publish(MobMoveEvent(buf.mob, buf.room, Direction.NORTH))
            return Response(buf, "you move north.")
        })
}
