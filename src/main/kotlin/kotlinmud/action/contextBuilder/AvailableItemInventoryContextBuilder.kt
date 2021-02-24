package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO

class AvailableItemInventoryContextBuilder(
    private val mob: Mob,
    private val room: RoomDAO
) : ContextBuilder {
    companion object {
        fun isMatch(item: ItemDAO, word: String): Boolean {
            return word.matches(item.name)
        }
    }

    override fun build(syntax: Syntax, word: String): Context<Any> {
        return tryInventory(mob.items, syntax, word)
            ?: tryInventory(room.items.toList(), syntax, word)
            ?: Context<Any>(
                syntax,
                Status.ERROR,
                "you don't see that anywhere."
            )
    }

    private fun tryInventory(items: List<ItemDAO>, syntax: Syntax, word: String): Context<Any>? {
        return items.find {
            isMatch(it, word) && it.isContainer
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        }
    }
}
