package kotlinmud.action

import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createNorthAction(): Action {
    return Action(
        Command.NORTH,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.COMMAND),
        { buf -> Response(buf, "you move north.") })
}
