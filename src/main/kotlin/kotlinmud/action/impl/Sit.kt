package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.mob.Disposition

fun createSitAction(): Action {
    return Action(Command.SIT, listOf(Disposition.SLEEPING, Disposition.STANDING)) {
        it.getMob().disposition = Disposition.SITTING
        it.createResponse(
            Message("you sit down.", "${it.getMob()} sits down."))
    }
}
