package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeStanding
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.mob.Mob

fun createHealAction(): Action {
    return Action(
        Command.HEAL,
        mustBeStanding(),
        listOf(Syntax.COMMAND, Syntax.SPELL_FROM_HEALER),
        { svc: ActionContextService, _: Request ->
            val skillType = svc.get<Mob>(Syntax.SPELL_FROM_HEALER)
            svc.createResponse(
                Message(
                    "success: $skillType"
                )
            )
        })
}
