package kotlinmud.action.impl.admin.area

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithFreeform
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Area
import kotlinmud.room.type.Lighting

fun createAreaAddAction(): Action {
    return ActionBuilder(Command.AREA_ADD).also {
        it.syntax = subcommandWithFreeform()
    } build {
        val areaName = it.get<String>(Syntax.FREE_FORM)
        it.addArea(
            Area(
                it.getAreas().size + 1,
                areaName,
                Lighting.Good,
            )
        )
        it.createOkResponse(
            messageToActionCreator("$areaName added")
        )
    }
}
