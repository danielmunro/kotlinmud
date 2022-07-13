package kotlinmud.action.impl.admin.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.roomRemoveDescription
import kotlinmud.io.type.Syntax
import kotlinmud.mob.type.Role

fun createRoomDescriptionRemoveAction(): Action {
    return ActionBuilder(Command.ROOM_DESCRIPTION_REMOVE).also {
        it.syntax = roomRemoveDescription()
        it.minimumRole = Role.Admin
    } build {
        val lineToRemove = it.get<Int>(Syntax.VALUE) - 1
        it.removeRoomDescription(it.getRoom().id, lineToRemove)
        it.createOkResponse(messageToActionCreator("room description line removed at $lineToRemove"))
    }
}
