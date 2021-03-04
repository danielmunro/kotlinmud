package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator

fun createTimeAction(): Action {
    return Action(Command.TIME) {
        it.createOkResponse(
            messageToActionCreator(it.getDate())
        )
    }
}
