package kotlinmud.action.impl.admin.area

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.areaToSet
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax
import kotlinmud.room.type.Area

fun createAreaSetAction(): Action {
    return ActionBuilder(Command.AREA_SET).also {
        it.syntax = areaToSet()
    } build {
        val input = it.get<String>(Syntax.FREE_FORM)
        Area.values().find { area -> area.name.toLowerCase().startsWith(input) }?.let { area ->
            it.getRoom().area = area
            it.createOkResponse(messageToActionCreator("area set"))
        } ?: it.createErrorResponse(messageToActionCreator("that is not a known area in this realm"))
    }
}
