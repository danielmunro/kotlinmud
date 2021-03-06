package kotlinmud.action.impl.disposition

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createSitMessage
import kotlinmud.mob.type.Disposition

fun createSitAction(): Action {
    return Action(Command.SIT, listOf(Disposition.SLEEPING, Disposition.STANDING)) {
        it.setDisposition(Disposition.SITTING)
        it.createOkResponse(createSitMessage(it.getMob()))
    }
}
