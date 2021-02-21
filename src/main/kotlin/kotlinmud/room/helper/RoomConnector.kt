package kotlinmud.room.helper

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction
import org.jetbrains.exposed.sql.transactions.transaction

class RoomConnector(private val room: RoomDAO) {
    fun to(connections: List<Pair<RoomDAO, Direction>>) {
        connections.forEach {
            to(it.first, it.second)
        }
    }

    fun to(connection: RoomDAO): RoomConnector {
        val directions = listOf(
            Direction.NORTH,
            Direction.SOUTH,
            Direction.EAST,
            Direction.WEST,
            Direction.UP,
            Direction.DOWN
        ).shuffled()

        var index = 0
        val exits = room.getAllExits()

        while (index < directions.size) {
            val direction = directions[index]
            if (!exits.containsKey(direction)) {
                return to(connection, direction)
            }
            index++
        }
        throw Exception("no connecting room")
    }

    fun to(connection: RoomDAO, direction: Direction): RoomConnector {
        transaction {
            when (direction) {
                Direction.DOWN -> {
                    room.down = connection
                    connection.up = room
                }
                Direction.UP -> {
                    room.up = connection
                    connection.down = room
                }
                Direction.NORTH -> {
                    room.north = connection
                    connection.south = room
                }
                Direction.SOUTH -> {
                    room.south = connection
                    connection.north = room
                }
                Direction.EAST -> {
                    room.east = connection
                    connection.west = room
                }
                Direction.WEST -> {
                    room.west = connection
                    connection.east = room
                }
            }
        }

        return RoomConnector(connection)
    }
}
