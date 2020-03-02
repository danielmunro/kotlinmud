package kotlinmud.action.impl

import kotlinmud.Noun
import kotlinmud.action.Action
import kotlinmud.action.ActionContextService
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createLookAtAction(): Action {
    return Action(
        Command.LOOK,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.AVAILABLE_NOUN),
        { svc: ActionContextService, _: Request ->
            createResponseWithEmptyActionContext(
                Message(svc.get<Noun>(Syntax.AVAILABLE_NOUN).description))
        })
}
