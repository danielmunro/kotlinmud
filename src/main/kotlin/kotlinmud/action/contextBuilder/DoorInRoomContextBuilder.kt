package kotlinmud.action.contextBuilder

import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.helper.string.matches
import kotlinmud.io.type.Syntax
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction

class DoorInRoomContextBuilder(private val room: RoomDAO) : ContextBuilder {
    override fun build(syntax: Syntax, word: String): Context<Any> {
        val doors = room.getDoors()
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
    if (word.matches(door.name)) {
        return true
    }

    if (word.matches(direction.value)) {
        return true
    }

    return false
}
