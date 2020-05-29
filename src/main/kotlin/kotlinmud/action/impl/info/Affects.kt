package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.io.messageToActionCreator

fun createAffectsAction(): Action {
    return Action(Command.AFFECTS) { svc ->
        createResponseWithEmptyActionContext(
            messageToActionCreator(
                "You are affected by:\n${svc.getAffects().fold("") { acc, it ->
                    acc + "${it.affectType.value}: ${it.timeout} tick${if (it.timeout == 1) "" else "s"}\n"
                }}"
            )
        )
    }
}
