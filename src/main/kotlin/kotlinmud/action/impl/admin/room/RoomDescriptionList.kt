package kotlinmud.action.impl.admin.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.roomListDescription
import kotlinmud.mob.type.Role

fun createRoomDescriptionListAction(): Action {
    return ActionBuilder(Command.ROOM_DESCRIPTION_LIST).also {
        it.syntax = roomListDescription()
        it.minimumRole = Role.Admin
    } build {
        val room = it.getRoom()
        val lines = room.description.split("\n").toMutableList()
        for (line in lines.indices) {
            lines[line] = "#${line + 1} ${lines[line]}"
        }
        val description = lines.joinToString("\n")
        it.createOkResponse(messageToActionCreator(description))
    }
}
