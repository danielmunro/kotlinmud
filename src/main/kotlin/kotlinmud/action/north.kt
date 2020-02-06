package kotlinmud.action

import kotlinmud.EventService
import kotlinmud.io.Buffer
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createNorthAction(): Action {
    return Action(
        Command.NORTH,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.COMMAND),
        fun (_: EventService, buf: Buffer): Response {
            return Response(buf, "you move north.")
        })
}
