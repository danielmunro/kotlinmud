package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommandWithModifier
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.repository.findMobInRoomWithJobType
import kotlinmud.mob.type.JobType

fun createQuestAcceptAction(): Action {
    return Action(
        Command.QUEST_ACCEPT,
        mustBeAlert(),
        subcommandWithModifier(),
    ) { svc ->
        findMobInRoomWithJobType(svc.getRoom(), JobType.QUEST)?.let {
            svc.createOkResponse(
                MessageBuilder()
                    .toActionCreator(svc.getQuests().joinToString { it.name + "\n" })
                    .build()
            )
        } ?: svc.createErrorResponse(messageToActionCreator("you don't see a quest giver here."))
    }
}
