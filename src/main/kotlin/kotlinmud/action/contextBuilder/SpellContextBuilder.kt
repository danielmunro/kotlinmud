package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.Spell

class SpellContextBuilder(private val skills: List<Skill>) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return skills.find {
            it is Spell && (word == it.type.value || word.matches(it.type.value))
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "you don't know that.")
    }
}
