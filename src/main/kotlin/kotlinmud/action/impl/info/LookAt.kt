package kotlinmud.action.impl.info

import kotlinmud.Noun
import kotlinmud.action.Action
import kotlinmud.action.mustBeAwake
import kotlinmud.action.type.Command
import kotlinmud.io.Syntax
import kotlinmud.io.availableNoun
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.io.messageToActionCreator

fun createLookAtAction(): Action {
    return Action(Command.LOOK, mustBeAwake(), availableNoun()) {
        createResponseWithEmptyActionContext(
            messageToActionCreator(it.get<Noun>(Syntax.AVAILABLE_NOUN).description)
        )
    }
}
