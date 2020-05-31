package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.affect.type.AffectType
import kotlinmud.io.Syntax
import kotlinmud.item.ItemService
import kotlinmud.item.type.Food
import kotlinmud.mob.model.Mob

class AvailableFoodContextBuilder(private val itemService: ItemService, private val mob: Mob) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = itemService.findByOwner(mob, word) ?: return notFound(syntax)

        if (target.affects().findByType(AffectType.INVISIBILITY) != null) {
            return notFound(syntax)
        }

        if (target.food == Food.NONE) {
            return Context(syntax, Status.ERROR, "That's not food.")
        }

        return Context(syntax, Status.OK, target)
    }
}
