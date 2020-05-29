package kotlinmud.action.impl.info

import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.io.messageToActionCreator
import kotlinmud.world.room.exit.DoorDisposition

fun createExitsAction(): Action {
    return Action(Command.EXITS) { svc ->
        createResponseWithEmptyActionContext(
            messageToActionCreator("Exits include:\n${svc.getExits().fold("") { acc, it ->
                acc + if (it.door != null && it.door.disposition != DoorDisposition.OPEN) "\n${it.direction.value} - ${it.door}" else "\n${it.direction.value} - ${it.destination.name}"
            }}")
        )
    }
}
