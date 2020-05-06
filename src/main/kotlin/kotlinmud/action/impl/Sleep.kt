package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.Message
import kotlinmud.mob.Disposition

fun createSleepAction(): Action {
    return Action(Command.SLEEP, listOf(Disposition.STANDING, Disposition.SITTING)) {
        it.getMob().disposition = Disposition.SLEEPING
        it.createResponse(
            Message("you lay down and go to sleep.", "${it.getMob()} lays down and goes to sleep.")
        )
    }
}
