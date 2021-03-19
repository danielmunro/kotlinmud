package kotlinmud.action.impl.quest

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.io.factory.submittableQuest
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.quest.type.Quest

fun createQuestSubmitAction(): Action {
    return Action(
        Command.QUEST_SUBMIT,
        mustBeAlert(),
        submittableQuest(),
    ) { svc ->
        val quest = svc.get<Quest>(Syntax.SUBMITTABLE_QUEST)
        svc.submitQuest(quest)
        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator("you submit the quest: `${quest.name}`")
                .toObservers("${svc.getMob()} submits the quest: `${quest.name}`")
                .build()
        )
    }
}
