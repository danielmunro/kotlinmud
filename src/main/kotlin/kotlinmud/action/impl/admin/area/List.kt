package kotlinmud.action.impl.admin.area

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommand

fun createAreaListAction(): Action {
    return ActionBuilder(Command.AREA_LIST).also {
        it.syntax = subcommand()
    } build {
        it.createOkResponse(
            messageToActionCreator(
                "Areas:\n\n" + it.getAreas().joinToString("\n") { area -> area.name }
            )
        )
    }
}
