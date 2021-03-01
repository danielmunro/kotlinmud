package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class AvailableItemInventoryContextBuilder(
    private val mob: Mob,
    private val room: RoomDAO
) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return tryInventory(mob.items, syntax, word)
//            ?: tryInventory(transaction { room.items.toList() }, syntax, word)
            ?: Context<Any>(
                syntax,
                Status.ERROR,
                "you don't see that anywhere."
            )
    }

    private fun tryInventory(items: List<Item>, syntax: Syntax, word: String): Context<Any>? {
        return items.find {
            word.matches(it.name) && it.isContainer
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        }
    }
}
