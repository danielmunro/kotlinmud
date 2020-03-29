package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createAffectsAction(): Action {
    return Action(
        Command.AFFECTS,
        mustBeAlert(),
        listOf(Syntax.COMMAND),
        { _: ActionContextService, request: Request ->
            createResponseWithEmptyActionContext(
                Message(
                    "You are affected by:\n${request.mob.affects.fold("") { acc, it -> acc + "${it.affectType.value}: ${it.timeout} tick${if (it.timeout == 1) "" else "s"}\n" }}"
                )
            )
        })
}
