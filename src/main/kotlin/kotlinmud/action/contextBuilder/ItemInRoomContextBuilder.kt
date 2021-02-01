package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.room.dao.RoomDAO

class ItemInRoomContextBuilder(private val itemService: ItemService, private val room: RoomDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return itemService.findByRoom(room, word)
            ?.let { Context<Any>(syntax, Status.OK, it) }
            ?: Context<Any>(
                syntax,
                Status.FAILED,
                "you don't see that anywhere."
            )
    }
}
