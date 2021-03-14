package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator

fun createExitsAction(): Action {
    return Action(Command.EXITS) { svc ->
        svc.createOkResponse(
            messageToActionCreator(
                "Exits include:\n${svc.getExits().entries.fold("") { acc, it ->
                    acc + if (svc.getRoom().isDoorPassable(it.key))
                        "\n${it.key.value} - ${it.value.name}"
                    else
                        "\n${it.key.value} - ${svc.getRoom().getDoorForDirection(it.key)?.name}"
                }}"
            )
        )
    }
}
