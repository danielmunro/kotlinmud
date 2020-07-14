package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO

class AvailableItemInventoryContextBuilder(private val mob: MobDAO, private val room: RoomDAO, private val itemService: ItemService) : ContextBuilder {
    companion object {
        fun isMatch(item: ItemDAO, word: String): Boolean {
            return kotlinmud.helper.string.matches(item.name, word)
        }
    }

    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemService.findAllByOwner(mob).find {
            isMatch(it, word) && it.isContainer
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: itemService.findAllByOwner(room).find {
            isMatch(it, word) && it.isContainer
        }?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(
            syntax,
            Status.ERROR,
            "you don't see anywhere to put that."
        )
    }
}
