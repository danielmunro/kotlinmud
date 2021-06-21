package kotlinmud.action.impl.quest

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.availableQuest
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.quest.model.Quest

fun createQuestAcceptAction(): Action {
    return ActionBuilder(Command.QUEST_ACCEPT).also {
        it.syntax = availableQuest()
    } build { svc ->
        val quest = svc.get<Quest>(Syntax.AVAILABLE_QUEST)
        svc.acceptQuest(quest)
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator("you accept the quest: `${quest.name}`")
                .toObservers("${svc.getMob()} accepts the quest: `${quest.name}`")
                .build()
        )
    }
}
