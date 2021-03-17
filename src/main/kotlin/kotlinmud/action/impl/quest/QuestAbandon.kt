package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.acceptedQuest
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.quest.type.Quest

fun createQuestAbandonAction(): Action {
    return Action(Command.QUEST_ABANDON, mustBeAlert(), acceptedQuest()) { svc ->
        val quest = svc.get<Quest>(Syntax.ACCEPTED_QUEST)
        svc.abandonQuest(quest)
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator("you abandon the quest: `${quest.name}`")
                .toObservers("${svc.getMob()} abandons the quest: `${quest.name}`")
                .build()
        )
    }
}
