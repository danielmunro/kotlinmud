package kotlinmud.action.impl.room

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommand

fun createOwnerInfoAction(): Action {
    return Action(Command.OWNER_INFO, mustBeAlert(), subcommand()) {
        it.createOkResponse(messageToActionCreator("this room is owned by ${it.getRoom().owner ?: "no one"}."))
    }
}
