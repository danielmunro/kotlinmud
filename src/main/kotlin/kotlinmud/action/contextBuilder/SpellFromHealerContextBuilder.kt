package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.type.JobType

class SpellFromHealerContextBuilder(private val mobs: List<MobDAO>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val mob = mobs.find {
            it.job == JobType.HEALER
        } ?: return Context<Any>(
            syntax,
            Status.ERROR,
            "you don't see any healers here."
        )
        return mob.skills.find {
            kotlinmud.helper.string.matches(it.type.name, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "they don't know that.")
    }
}
