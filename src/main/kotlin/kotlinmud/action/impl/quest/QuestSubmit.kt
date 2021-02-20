package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.helper.string.matches
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithModifier
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax

fun createQuestSubmitAction(): Action {
    return Action(
        Command.QUEST_SUBMIT,
        mustBeAlert(),
        subcommandWithModifier(),
    ) { svc ->
        val input = svc.get<String>(Syntax.FREE_FORM)
        svc.getSubmittableQuests().find {
            input.matches(it.name)
        }?.let {
            svc.submitQuest(it)
            svc.createOkResponse(
                MessageBuilder()
                    .toActionCreator("you submit the quest: `${it.name}`")
                    .toObservers("${svc.getMob()} submits the quest: `${it.name}`")
                    .build()
            )
        } ?: svc.createErrorResponse(messageToActionCreator("you can't find that quest."))
    }
}
