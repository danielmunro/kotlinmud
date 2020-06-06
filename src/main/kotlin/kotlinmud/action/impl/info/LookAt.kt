package kotlinmud.action.impl.info

import kotlinmud.Noun
import kotlinmud.action.helper.mustBeAwake
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.availableNoun
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.io.type.Syntax

fun createLookAtAction(): Action {
    return Action(Command.LOOK, mustBeAwake(), availableNoun()) {
        createResponseWithEmptyActionContext(
            messageToActionCreator(it.get<Noun>(Syntax.AVAILABLE_NOUN).description)
        )
    }
}
