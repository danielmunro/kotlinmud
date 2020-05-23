package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.mob.type.Disposition

fun createSitAction(): Action {
    return Action(Command.SIT, listOf(Disposition.SLEEPING, Disposition.STANDING)) {
        it.getMob().disposition = Disposition.SITTING
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you sit down.")
                .toObservers("${it.getMob()} sits down.")
                .build()
        )
    }
}
