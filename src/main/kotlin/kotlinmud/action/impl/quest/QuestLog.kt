package kotlinmud.action.impl.quest

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommand

fun createQuestLogAction(): Action {
    return ActionBuilder(Command.QUEST_LOG).also {
        it.syntax = subcommand()
    } build { svc ->
        svc.createOkResponse(
            messageToActionCreator(
                svc.getQuestLog().joinToString { it.name + "\n" }
            )
        )
    }
}
