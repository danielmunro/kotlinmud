package kotlinmud.action.impl.admin

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createSitMessage
import kotlinmud.mob.type.Disposition

fun createBanAction(): Action {
    return Action(Command.BAN) {
        it.setDisposition(Disposition.SITTING)
        it.createOkResponse(createSitMessage(it.getMob()))
    }
}
