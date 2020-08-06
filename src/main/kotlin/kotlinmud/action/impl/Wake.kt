package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createWakeMessage
import kotlinmud.mob.type.Disposition

fun createWakeAction(): Action {
    return Action(
        Command.WAKE,
        listOf(Disposition.SLEEPING, Disposition.SITTING)
    ) {
        it.getMob().disposition = Disposition.STANDING
        it.createOkResponse(createWakeMessage(it.getMob()))
    }
}
