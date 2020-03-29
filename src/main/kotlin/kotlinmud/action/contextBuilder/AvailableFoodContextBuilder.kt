package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.affect.AffectType
import kotlinmud.io.Syntax
import kotlinmud.item.Food
import kotlinmud.mob.Mob

class AvailableFoodContextBuilder(private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = mob.inventory.items.find {
            kotlinmud.string.matches(it.name, word)
        } ?: return notFound(syntax)

        if (target.affects().findByType(AffectType.INVISIBILITY) != null) {
            return notFound(syntax)
        }

        if (target.food == Food.NONE) {
            return Context(syntax, Status.ERROR, "That's not food.")
        }

        return Context(syntax, Status.OK, target)
    }
}
