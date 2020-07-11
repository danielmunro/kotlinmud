package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Food
import kotlinmud.mob.dao.MobDAO

class AvailableFoodContextBuilder(private val itemService: ItemService, private val mob: MobDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = itemService.findByOwner(mob, word) ?: return notFound(syntax)

        target.affects.find { it.type == AffectType.INVISIBILITY }?.let {
            return notFound(syntax)
        }

        if (target.food == Food.NONE) {
            return Context(syntax, Status.ERROR, "That's not food.")
        }

        return Context(syntax, Status.OK, target)
    }
}
