package kotlinmud.action

import kotlinmud.EventService
import kotlinmud.event.MobMoveEvent
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.room.Direction

fun createSouthAction(): Action {
    return Action(
        Command.SOUTH,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.COMMAND),
        fun (eventService: EventService, buf: Request): Response {
            eventService.publish(MobMoveEvent(buf.mob, buf.room, Direction.SOUTH))
            return Response(buf, "you move south.")
        })
}
