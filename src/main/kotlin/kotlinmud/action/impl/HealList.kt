package kotlinmud.action.impl

import kotlinmud.action.helper.mustBeStanding
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.command
import kotlinmud.io.factory.messageToActionCreator

fun createHealListAction(): Action {
    return Action(Command.HEAL, mustBeStanding(), command()) {
        it.createOkResponse(messageToActionCreator("success, healer"))
    }
}
