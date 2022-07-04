package kotlinmud.action.impl.admin.area

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithFreeform
import kotlinmud.io.type.Syntax

fun createAreaAddAction(): Action {
    return ActionBuilder(Command.AREA_ADD).also {
        it.syntax = subcommandWithFreeform()
    } build {
        val area = it.get<String>(Syntax.FREE_FORM)
        it.addArea(area)
        it.createOkResponse(
            messageToActionCreator("$area added")
        )
    }
}
