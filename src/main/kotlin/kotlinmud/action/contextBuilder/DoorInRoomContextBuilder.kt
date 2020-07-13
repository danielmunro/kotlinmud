package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction
import org.jetbrains.exposed.sql.transactions.transaction

class DoorInRoomContextBuilder(private val room: RoomDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val doors = room.getDoors()
        val door = transaction { room.northDoor }
        return doors.entries.find {
            matchExit(it.key, it.value, word)
        }?.let { Context<Any>(syntax, Status.OK, it.value) }
            ?: Context<Any>(
                syntax,
                Status.FAILED,
                "you don't see that anywhere."
            )
    }
}

fun matchExit(direction: Direction, door: DoorDAO, word: String): Boolean {
    if (matches(door.name, word)) {
        return true
    }

    if (matches(direction.value, word)) {
        return true
    }

    return false
}
