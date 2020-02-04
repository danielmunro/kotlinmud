package kotlinmud.action

import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createSouthAction(): Action {
    return Action(
        Command.SOUTH,
        arrayOf(Disposition.STANDING),
        arrayOf(Syntax.COMMAND),
        { buf -> Response(buf, "you move south.") })
}
