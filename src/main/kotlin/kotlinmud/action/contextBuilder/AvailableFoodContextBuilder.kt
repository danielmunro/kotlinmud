package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob

class AvailableFoodContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = mob.items.find { word.matches(it.name) } ?: return notFound(syntax)

        if (!target.isVisible()) {
            return notFound(syntax)
        }

        if (!target.isFood()) {
            return Context(syntax, Status.ERROR, "That's not food.")
        }

        return Context(syntax, Status.OK, target)
    }
}
