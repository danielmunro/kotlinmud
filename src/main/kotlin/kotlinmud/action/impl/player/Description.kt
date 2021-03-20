package kotlinmud.action.impl.player

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.freeForm
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.Syntax

fun createDescriptionAction(): Action {
    return ActionBuilder(Command.DESCRIPTION).also {
        it.syntax = freeForm()
    } build {
        val description = it.get<String>(Syntax.FREE_FORM)
        it.getMob().description = description
        it.createOkResponse(messageToActionCreator("Your description is updated."))
    }
}
