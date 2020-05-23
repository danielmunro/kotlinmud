package kotlinmud.action.contextBuilder

import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.io.Syntax
import kotlinmud.item.Item
import kotlinmud.item.ItemService
import kotlinmud.mob.Mob
import kotlinmud.world.room.Room

class AvailableItemInventoryContextBuilder(private val mob: Mob, private val room: Room, private val itemService: ItemService) : ContextBuilder {
    companion object {
        fun isMatch(item: Item, word: String): Boolean {
            return item.hasInventory && kotlinmud.string.matches(item.name, word)
        }
    }

    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemService.findAllByOwner(mob).find {
            isMatch(it, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: itemService.findAllByOwner(room).find {
            isMatch(it, word)
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(syntax, Status.ERROR, "you don't see anywhere to put that.")
    }
}
