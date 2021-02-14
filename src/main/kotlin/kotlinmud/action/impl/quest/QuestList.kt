package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.subcommand
import kotlinmud.io.model.MessageBuilder

fun createQuestListAction(): Action {
    return Action(Command.QUEST_LIST, mustBeAlert(), subcommand()) { svc ->
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator(svc.getAcceptableQuests().joinToString { it.name + "\n" })
                .build()
        )
    }
}
