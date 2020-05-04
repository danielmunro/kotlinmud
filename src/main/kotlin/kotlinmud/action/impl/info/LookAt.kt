package kotlinmud.action.impl.info

import kotlinmud.Noun
import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAwake
import kotlinmud.io.Message
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext

fun createLookAtAction(): Action {
    return Action(
        Command.LOOK,
        mustBeAwake(),
        listOf(Syntax.COMMAND, Syntax.AVAILABLE_NOUN)) {
            createResponseWithEmptyActionContext(
                Message(it.get<Noun>(Syntax.AVAILABLE_NOUN).description))
        }
}
