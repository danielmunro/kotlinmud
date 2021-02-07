package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommand

fun createQuestLogAction(): Action {
    return Action(Command.QUEST_LOG, mustBeAlert(), subcommand()) { svc ->
        svc.createOkResponse(
            messageToActionCreator(
                svc.getQuestLog().joinToString { it.name + "\n" }
            )
        )
    }
}
