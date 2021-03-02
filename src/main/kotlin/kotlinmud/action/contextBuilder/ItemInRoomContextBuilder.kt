package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.room.model.Room
import org.jetbrains.exposed.sql.transactions.transaction

class ItemInRoomContextBuilder(private val room: Room) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        return transaction {
            room.items.find {
                word.matches(it.name)
            }?.let { Context<Any>(syntax, Status.OK, it) }
                ?: Context<Any>(
                    syntax,
                    Status.FAILED,
                    "you don't see that anywhere."
                )
        }
    }
}
