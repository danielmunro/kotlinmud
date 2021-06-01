package kotlinmud.action.impl.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.doorInRoom
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommand
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Door
import kotlinmud.room.type.DoorDisposition

fun createLockAction(): Action {
    return ActionBuilder(Command.LOCK).also {
        it.syntax = doorInRoom()
    } build {
        val door = it.get<Door>(Syntax.DOOR_IN_ROOM)

        it.getMob().items.find { item -> item.canonicalId == door.key }
            ?: return@build it.createErrorResponse(
                messageToActionCreator("you lack the key.")
            )

        door.disposition = DoorDisposition.LOCKED

        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you lock $door.")
                .toObservers("${it.getMob()} locks $door.")
                .build()
        )
    }
}
