package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.mob.Disposition

fun createSleepAction(): Action {
    return Action(Command.SLEEP, listOf(Disposition.STANDING, Disposition.SITTING)) {
        it.getMob().disposition = Disposition.SLEEPING
        it.createResponse(
            MessageBuilder()
                .toActionCreator("you lay down and go to sleep.")
                .toObservers("${it.getMob()} lays down and goes to sleep.")
                .build()
        )
    }
}
