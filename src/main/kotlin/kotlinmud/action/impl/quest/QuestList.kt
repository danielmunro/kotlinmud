package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.model.MessageBuilder

fun createQuestListAction(): Action {
    return Action(
        Command.QUEST_LIST,
        mustBeAlert(),
    ) {
        it.createOkResponse(MessageBuilder().build())
    }
}
