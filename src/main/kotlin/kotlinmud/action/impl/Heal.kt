package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

fun createHealAction(): Action {
    return Action(
        Command.HEAL,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.SPELL_FROM_HEALER),
        {
            val skillType = it.get<Mob>(Syntax.SPELL_FROM_HEALER)
            it.createResponse(
                Message(
                    "success: $skillType"
                )
            )
        })
}
