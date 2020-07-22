package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.createResponseWithEmptyActionContext

fun createExitsAction(): Action {
    return Action(Command.EXITS) { svc ->
        createResponseWithEmptyActionContext(
            messageToActionCreator(
                "Exits include:\n${svc.getExits().entries.fold("") { acc, it ->
                    acc + if (svc.getRoom().isDoorPassable(it.key)) "\n${it.key.value} - ${svc.getRoom().getDoorForDirection(it.key)?.name}" else "\n${it.key.value} - ${it.value.name}"
                }}"
            )
        )
    }
}
