package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeAlert
import kotlinmud.io.IOStatus
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
            DoorDisposition.LOCKED -> it.createResponse(messageToActionCreator("it is locked."), IOStatus.ERROR)
            DoorDisposition.OPEN -> it.createResponse(messageToActionCreator("it is already open."), IOStatus.ERROR)
            DoorDisposition.CLOSED -> {
                door.disposition = DoorDisposition.OPEN
                it.createResponse(
                    MessageBuilder()
                        .toActionCreator("you open $door.")
                        .toObservers("${it.getMob()} opens $door.")
                        .build()
                )
            }
        }
    }
}
