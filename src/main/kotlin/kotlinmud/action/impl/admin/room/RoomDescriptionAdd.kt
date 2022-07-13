package kotlinmud.action.impl.admin.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.roomAddDescription
import kotlinmud.io.type.Syntax
import kotlinmud.mob.type.Role

fun createRoomDescriptionAddAction(): Action {
    return ActionBuilder(Command.ROOM_DESCRIPTION_ADD).also {
        it.syntax = roomAddDescription()
        it.minimumRole = Role.Admin
    } build {
        it.addToRoomDescription(it.getRoom().id, it.get(Syntax.FREE_FORM))
        it.createOkResponse(messageToActionCreator("added to room description"))
    }
}
