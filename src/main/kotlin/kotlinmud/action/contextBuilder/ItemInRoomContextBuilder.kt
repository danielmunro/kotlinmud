package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.Syntax
import kotlinmud.item.ItemService
import kotlinmud.room.model.Room

class ItemInRoomContextBuilder(private val itemService: ItemService, private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemService.findByOwner(room, word)
                ?.let { Context<Any>(syntax, Status.OK, it) }
            ?: Context<Any>(
                syntax,
                Status.FAILED,
                "you don't see that anywhere."
            )
    }
}
