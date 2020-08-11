package kotlinmud.action.impl.disposition

import kotlinmud.action.helper.mustBeResting
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createWakeMessage
import kotlinmud.mob.type.Disposition

fun createWakeAction(): Action {
    return Action(Command.WAKE, mustBeResting()) {
        it.setDisposition(Disposition.STANDING)
        it.createOkResponse(createWakeMessage(it.getMob()))
    }
}
