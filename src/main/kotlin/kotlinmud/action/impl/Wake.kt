package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.mob.type.Disposition

fun createWakeAction(): Action {
    return Action(Command.WAKE, listOf(Disposition.SLEEPING, Disposition.SITTING)) {
        it.getMob().disposition = Disposition.STANDING
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you stand up.")
                .toObservers("${it.getMob()} stands up.")
                .build()
        )
    }
}
