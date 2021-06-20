package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.PlayerMob
import kotlinmud.quest.service.QuestService

class SubmittableQuestContextBuilder(private val questService: QuestService, private val mob: PlayerMob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return questService.getSubmittableQuestsForMob(mob).find { word.matches(it.name) }?.let {
            Context(Syntax.SUBMITTABLE_QUEST, Status.OK, it)
        } ?: questService.getAcceptedQuestsForMob(mob).find { word.matches(it.name) }?.let {
            Context(Syntax.SUBMITTABLE_QUEST, Status.ERROR, "that quest is not ready to submit.")
        } ?: Context(Syntax.SUBMITTABLE_QUEST, Status.ERROR, "you can't find that quest.")
    }
}
