package kotlinmud.action.impl.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.doorInRoom
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Door
import kotlinmud.room.type.DoorDisposition

fun createUnlockAction(): Action {
    return ActionBuilder(Command.UNLOCK).also {
        it.syntax = doorInRoom()
    } build {
        val door = it.get<Door>(Syntax.DOOR_IN_ROOM)

        it.findMobItem { item -> item.keyId == door.keyId }
            ?: return@build it.createErrorResponse(
                messageToActionCreator("you lack the key.")
            )

        if (door.disposition != DoorDisposition.LOCKED) {
            return@build it.createErrorResponse(
                messageToActionCreator("the door is not locked.")
            )
        }

        door.disposition = DoorDisposition.CLOSED

        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you unlock $door.")
                .toObservers("${it.getMob()} unlocks $door.")
                .build()
        )
    }
}
