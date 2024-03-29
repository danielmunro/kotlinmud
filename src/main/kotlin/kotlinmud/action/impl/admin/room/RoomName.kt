package kotlinmud.action.impl.admin.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.propToSet
import kotlinmud.io.type.Syntax
import kotlinmud.mob.type.Role

fun createRoomNameAction(): Action {
    return ActionBuilder(Command.ROOM_NAME).also {
        it.syntax = propToSet()
        it.minimumRole = Role.Admin
    } build {
        it.getRoom().name = it.get(Syntax.FREE_FORM)
        it.createOkResponse(messageToActionCreator("room renamed"))
    }
}
