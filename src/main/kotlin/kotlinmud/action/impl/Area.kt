package kotlinmud.action.impl

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.command
import kotlinmud.io.factory.createAreaMessage

fun createAreaAction(): Action {
    return ActionBuilder(Command.AREA).also {
        it.syntax = command()
    } build {
        it.createOkResponse(createAreaMessage(it.getRoom()))
    }
}
