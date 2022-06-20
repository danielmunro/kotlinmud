package kotlinmud.action.impl.admin

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator

fun createFlushAction(): Action {
    return ActionBuilder(Command.FLUSH).build {
        it.flush()
        it.createOkResponse(messageToActionCreator("world persisted"))
    }
}
