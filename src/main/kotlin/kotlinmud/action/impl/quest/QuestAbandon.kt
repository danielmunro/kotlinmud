package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.helper.string.matches
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithModifier
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax

fun createQuestAbandonAction(): Action {
    return Action(Command.QUEST_ABANDON, mustBeAlert(), subcommandWithModifier()) { svc ->
        val input = svc.get<String>(Syntax.FREE_FORM)
        svc.getAcceptedQuests().find {
            input.matches(it.name)
        }?.let {
            svc.abandonQuest(it)
            svc.createOkResponse(
                MessageBuilder()
                    .toActionCreator("you abandon the quest: `${it.name}`")
                    .toObservers("${svc.getMob()} abandons the quest: `${it.name}`")
                    .build()
            )
        } ?: svc.createErrorResponse(messageToActionCreator("you don't know that quest."))
    }
}
