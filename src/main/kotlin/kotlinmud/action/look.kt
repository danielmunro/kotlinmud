package kotlinmud.action

import kotlinmud.EventService
import kotlinmud.io.Buffer
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createLookAction(): Action {
    return Action(
        Command.LOOK,
        arrayOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING),
        arrayOf(Syntax.COMMAND),
        { _: EventService, buffer: Buffer -> Response(buffer, "look") })
}
