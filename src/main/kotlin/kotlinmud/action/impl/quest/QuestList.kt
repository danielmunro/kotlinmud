package kotlinmud.action.impl.quest

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.subcommand
import kotlinmud.io.model.MessageBuilder

fun createQuestListAction(): Action {
    return ActionBuilder(Command.QUEST_LIST).also {
        it.syntax = subcommand()
    } build { svc ->
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator(svc.getAcceptableQuests().joinToString { it.name + "\n" })
                .build()
        )
    }
}
