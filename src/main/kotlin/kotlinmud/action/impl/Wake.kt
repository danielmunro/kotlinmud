package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition

fun createWakeAction(): Action {
    return Action(
        Command.WAKE,
        listOf(Disposition.SLEEPING, Disposition.SITTING),
        listOf(Syntax.COMMAND)) {
            it.getMob().disposition = Disposition.STANDING
            it.createResponse(
                Message("you stand up.", "${it.getMob()} stands up."))
        }
}
