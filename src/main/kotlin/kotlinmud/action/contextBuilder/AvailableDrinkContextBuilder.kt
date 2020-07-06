package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.affect.table.Affects
import kotlinmud.affect.table.Affects.type
import kotlinmud.affect.type.AffectType
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Drink
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

const val notFound = "you don't see anything like that here."

class AvailableDrinkContextBuilder(
    private val itemService: ItemService,
    private val mob: MobDAO,
    private val room: RoomDAO
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val target = itemService.findByOwner(mob, word)
            ?: itemService.findByRoom(room, word)
            ?: return notFound(syntax)

        target.affects.find { it.type == AffectType.INVISIBILITY }?.let {
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
