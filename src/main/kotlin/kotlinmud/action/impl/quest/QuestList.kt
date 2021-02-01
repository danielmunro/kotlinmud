package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.repository.findMobInRoomWithJobType
import kotlinmud.mob.type.JobType

fun createQuestListAction(): Action {
    return Action(
        Command.QUEST_LIST,
        mustBeAlert(),
    ) { svc ->
        findMobInRoomWithJobType(svc.getRoom(), JobType.QUEST)?.let {
            svc.createOkResponse(
                MessageBuilder()
                    .toActionCreator("")
                    .build()
            )
        } ?: svc.createErrorResponse(
            MessageBuilder()
                .toActionCreator("you don't see a quest giver here.")
                .build()
        )
    }
}
