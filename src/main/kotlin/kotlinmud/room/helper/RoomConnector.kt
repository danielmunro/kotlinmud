package kotlinmud.room.helper

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction

class RoomConnector(private val room: RoomDAO) {
    fun to(connections: List<Pair<RoomDAO, Direction>>) {
        connections.forEach {
            to(it.first, it.second)
        }
    }

    fun to(connection: RoomDAO, direction: Direction): RoomConnector {
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
