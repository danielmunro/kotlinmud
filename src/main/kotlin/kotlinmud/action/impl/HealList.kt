package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.mustBeStanding
import kotlinmud.action.type.Command
import kotlinmud.io.command
import kotlinmud.io.messageToActionCreator

fun createHealListAction(): Action {
    return Action(Command.HEAL, mustBeStanding(), command()) {
        it.createOkResponse(messageToActionCreator("success, healer"))
    }
}
