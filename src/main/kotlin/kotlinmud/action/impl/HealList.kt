package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.command

fun createHealListAction(): Action {
    return Action(Command.HEAL, mustBeStanding(), command()) {
        it.createResponse(Message("success, healer"))
    }
}
