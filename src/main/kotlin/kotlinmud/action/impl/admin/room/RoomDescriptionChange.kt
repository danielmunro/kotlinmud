package kotlinmud.action.impl.admin.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.roomChangeDescription
import kotlinmud.io.type.Syntax
import kotlinmud.mob.type.Role

fun createRoomDescriptionChangeAction(): Action {
    return ActionBuilder(Command.ROOM_DESCRIPTION_CHANGE).also {
        it.syntax = roomChangeDescription()
        it.minimumRole = Role.Admin
    } build {
        val lineToChange = it.get<Int>(Syntax.VALUE)
        it.changeRoomDescription(it.getRoom().id, it.get(Syntax.FREE_FORM), lineToChange)
        it.createOkResponse(messageToActionCreator("room description changed at line $lineToChange"))
    }
}
