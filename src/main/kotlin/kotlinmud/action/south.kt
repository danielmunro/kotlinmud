package kotlinmud.action

import kotlinmud.EventService
import kotlinmud.io.Buffer
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createSouthAction(): Action {
    return Action(
        Command.SOUTH,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.COMMAND),
        { _: EventService, buffer: Buffer -> Response(buffer, "you move south.") })
}
