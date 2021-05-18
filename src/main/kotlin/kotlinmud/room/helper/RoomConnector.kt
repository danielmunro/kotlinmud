package kotlinmud.room.helper

import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction

class RoomConnector(private val room: Room) {
    fun toRoom(connections: List<Pair<Room, Direction>>): RoomConnector {
        lateinit var last: RoomConnector
        connections.forEach {
            last = toRoom(it.first, it.second)
        }
        return last
    }

    fun toRoom(connection: Room): RoomConnector {
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
                return toRoom(connection, direction)
            }
            index++
        }
        throw Exception("no connecting room")
    }

    fun toRoom(connection: Room, direction: Direction): RoomConnector {
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

        return RoomConnector(connection)
    }
}
