package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.service.QuestService

class AcceptedQuestContextBuilder(
    private val questService: QuestService,
    private val mob: PlayerMob,
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return questService.getAcceptedQuestsForMob(mob).find { word.matches(it.name) }?.let {
            Context(Syntax.ACCEPTED_QUEST, Status.OK, it)
        } ?: Context(Syntax.ACCEPTED_QUEST, Status.ERROR, "you don't know that quest.")
    }
}
