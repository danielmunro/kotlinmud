package kotlinmud.action.impl.admin.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.roomToClone
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.type.Role
import kotlinmud.room.type.Direction

fun createRoomCloneAction(): Action {
    return ActionBuilder(Command.ROOM_CLONE).also {
        it.syntax = roomToClone()
        it.minimumRole = Role.Admin
    } build {
        val direction = it.get<Direction>(Syntax.DIRECTION_WITH_NO_EXIT)
        it.addRoom(direction)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("You clone the room ${direction.value}.")
                .build()
        )
    }
}
