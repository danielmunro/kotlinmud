package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.affect.AffectType
import kotlinmud.io.Syntax
import kotlinmud.item.Drink
import kotlinmud.mob.Mob
import kotlinmud.service.ItemService
import kotlinmud.world.room.Room

const val notFound = "you don't see anything like that here."

class AvailableDrinkContextBuilder(
    private val itemService: ItemService,
    private val mob: Mob,
    private val room: Room
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = itemService.findByOwner(mob, word)
            ?: itemService.findByOwner(room, word)
            ?: return notFound(syntax)

        if (target.affects().findByType(AffectType.INVISIBILITY) != null) {
            return notFound(syntax)
        }

        if (target.drink == Drink.NONE) {
            return Context(syntax, Status.ERROR, "That's not a drink.")
        }

        if (target.quantity == 0) {
            return Context(syntax, Status.ERROR, "That is empty.")
        }

        return Context(syntax, Status.OK, target)
    }
}

fun notFound(syntax: Syntax): Context<Any> {
    return Context(syntax, Status.ERROR, notFound)
}
