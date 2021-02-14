package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.helper.string.matches
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithModifier
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.mob.repository.findMobInRoomWithJobType
import kotlinmud.mob.type.JobType

fun createQuestAcceptAction(): Action {
    return Action(
        Command.QUEST_ACCEPT,
        mustBeAlert(),
        subcommandWithModifier(),
    ) { svc ->
        val input = svc.get<String>(Syntax.FREE_FORM)
        svc.getAcceptableQuests().find {
            input.matches(it.name)
        }?.let {
            svc.acceptQuest(it)
            svc.createOkResponse(
                MessageBuilder()
                    .toActionCreator("you accept the quest: `${it.name}`")
                    .toObservers("${svc.getMob()} accepts the quest: `${it.name}`")
                    .build()
            )
        } ?: svc.createErrorResponse(messageToActionCreator("they cannot grant you that."))
    }
}
