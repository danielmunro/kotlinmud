package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.factory.subcommand
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.repository.findMobInRoomWithJobType
import kotlinmud.mob.type.JobType

fun createQuestListAction(): Action {
    return Action(Command.QUEST_LIST, mustBeAlert(), subcommand()) { svc ->
        findMobInRoomWithJobType(svc.getRoom(), JobType.QUEST)?.let {
            svc.createOkResponse(
                MessageBuilder()
                    .toActionCreator(svc.getAcceptableQuests().joinToString { it.name + "\n" })
                    .build()
            )
        } ?: svc.createErrorResponse(messageToActionCreator("you don't see a quest giver here."))
    }
}
