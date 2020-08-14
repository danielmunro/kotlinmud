package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO

class AvailableItemInventoryContextBuilder(
    private val mob: MobDAO,
    private val room: RoomDAO,
    private val itemService: ItemService
) : ContextBuilder {
    companion object {
        fun isMatch(item: ItemDAO, word: String): Boolean {
            return word.matches(item.name)
        }
    }

    override fun build(syntax: Syntax, word: String): Context<Any> {
        return tryInventory(mob, syntax, word)
            ?: tryInventory(room, syntax, word)
            ?: Context<Any>(
                syntax,
                Status.ERROR,
                "you don't see that anywhere."
            )
    }

    private fun tryInventory(hasInventory: HasInventory, syntax: Syntax, word: String): Context<Any>? {
        return itemService.findAllByOwner(hasInventory).find {
            isMatch(it, word) && it.isContainer
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        }
    }
}
