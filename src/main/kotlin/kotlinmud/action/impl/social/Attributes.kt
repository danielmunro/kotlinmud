package kotlinmud.action.impl.social

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.createAttributesMessage
import kotlinmud.io.model.createResponseWithEmptyActionContext

fun createAttributesAction(): Action {
    return Action(Command.ATTRIBUTES) {
        createResponseWithEmptyActionContext(createAttributesMessage(it.getMob()))
    }
}
