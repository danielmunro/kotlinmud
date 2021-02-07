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

fun createQuestAbandonAction(): Action {
    return Action(Command.QUEST_ABANDON, mustBeAlert(), subcommandWithModifier()) { svc ->
        findMobInRoomWithJobType(svc.getRoom(), JobType.QUEST)?.let {
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
            } ?: svc.createErrorResponse(messageToActionCreator("they cannot grant you that."))
        } ?: svc.createErrorResponse(messageToActionCreator("you don't see a quest giver here."))
    }
}