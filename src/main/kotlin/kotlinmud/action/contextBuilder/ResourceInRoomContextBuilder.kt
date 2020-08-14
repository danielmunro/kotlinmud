package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Resources
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class ResourceInRoomContextBuilder(private val room: RoomDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val resources = transaction {
            ResourceDAO.wrapRows(
                Resources.select {
                    Resources.roomId eq room.id
                }
            )
        }

        val resource = transaction { resources.find { word.matches(it.type.toString()) } }

        return resource?.let {
            Context<Any>(syntax, Status.OK, it)
        } ?: Context<Any>(
            syntax,
            Status.ERROR,
            "you don't see that anywhere."
        )
    }
}
