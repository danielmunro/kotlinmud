package kotlinmud.action.impl

import kotlinmud.action.model.Action
import kotlinmud.action.mustBeAlert
import kotlinmud.action.type.Command
import kotlinmud.io.MessageBuilder
import kotlinmud.io.Syntax
import kotlinmud.io.doorInRoom
import kotlinmud.io.messageToActionCreator
import kotlinmud.world.room.exit.Door
import kotlinmud.world.room.exit.DoorDisposition

fun createOpenAction(): Action {
    return Action(Command.OPEN, mustBeAlert(), doorInRoom()) {
        val door: Door = it.get(Syntax.DOOR_IN_ROOM)
        when (door.disposition) {
            DoorDisposition.LOCKED -> it.createErrorResponse(messageToActionCreator("it is locked."))
            DoorDisposition.OPEN -> it.createErrorResponse(messageToActionCreator("it is already open."))
            DoorDisposition.CLOSED -> {
                door.disposition = DoorDisposition.OPEN
                it.createOkResponse(
                    MessageBuilder()
                        .toActionCreator("you open $door.")
                        .toObservers("${it.getMob()} opens $door.")
                        .build()
                )
            }
        }
    }
}
