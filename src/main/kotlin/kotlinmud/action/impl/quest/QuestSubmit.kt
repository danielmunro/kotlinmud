package kotlinmud.action.impl.quest

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.submittableQuest
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.quest.model.Quest

fun createQuestSubmitAction(): Action {
    return ActionBuilder(Command.QUEST_SUBMIT).also {
        it.syntax = submittableQuest()
    } build {
        val quest = it.get<Quest>(Syntax.SUBMITTABLE_QUEST)
        it.submitQuest(quest)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("you submit the quest: `${quest.name}`")
                .toObservers("${it.getMob()} submits the quest: `${quest.name}`")
                .build()
        )
    }
}
