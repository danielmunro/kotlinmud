package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob

class SkillToPracticeContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val mobCard = mob.mobCard!!
        if (mobCard.practices == 0) {
            return Context(
                syntax,
                Status.ERROR,
                "you don't have any practices left."
            )
        }
        return mob.skills.keys.find {
            word.matches(it.toString())
        }?.let {
            if (mob.skills[it] == 100) {
                return Context(
                    syntax,
                    Status.ERROR,
                    "you cannot practice that anymore."
                )
            }
            Context(syntax, Status.OK, it)
        } ?: Context(syntax, Status.ERROR, "you don't know that.")
    }
}
