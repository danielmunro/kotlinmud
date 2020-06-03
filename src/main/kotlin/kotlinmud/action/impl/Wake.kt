package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.type.Disposition

fun createWakeAction(): Action {
    return Action(
        Command.WAKE,
        listOf(Disposition.SLEEPING, Disposition.SITTING)
    ) {
        it.getMob().disposition = Disposition.STANDING
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you stand up.")
                .toObservers("${it.getMob()} stands up.")
                .build()
        )
    }
}
