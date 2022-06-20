package kotlinmud.action.impl.admin.room

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.propToSet
import kotlinmud.io.type.Syntax
import kotlinmud.mob.type.Role
import kotlinmud.room.type.Area

fun createRoomAreaAction(): Action {
    return ActionBuilder(Command.ROOM_AREA).also {
        it.syntax = propToSet()
        it.minimumRole = Role.Admin
    } build {
        val input = it.get<String>(Syntax.FREE_FORM)
        Area.values().find { area -> area.name.toLowerCase().startsWith(input) }?.let { area ->
            it.getRoom().area = area
            it.createOkResponse(messageToActionCreator("area set"))
        } ?: it.createErrorResponse(messageToActionCreator("that is not a known area in this realm"))
    }
}
