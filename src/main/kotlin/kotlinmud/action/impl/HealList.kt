package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax

fun createHealListAction(): Action {
    return Action(
        Command.HEAL,
        mustBeStanding(),
        listOf(Syntax.COMMAND),
        {
            it.createResponse(Message("success, healer"))
        })
}
