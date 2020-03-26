package kotlinmud.path

import kotlinmud.room.Room

class Pathfinder(private val src: Room, private val dest: Room) {
    private val explored: MutableList<Explored> = mutableListOf()

    fun find(): List<Room> {
        return proceed(mutableListOf(src), 0) ?: listOf()
    }

    private fun proceed(rooms: MutableList<Room>, moves: Int): List<Room>? {
        if (moves > 10) {
            return null
        }
        rooms.forEach {
            if (it == dest) {
                return rooms.toList()
            }
            if (!roomExplored(it)) {
                if (!roomSeen(it)) {
                    explored.add(Explored(it, true))
                }
                it.exits.forEach { exit ->
                    val roomsToProceed = rooms.toMutableList()
                    roomsToProceed.add(exit.destination)
                    val dest = proceed(roomsToProceed, moves + 1)
                    if (dest != null) {
                        return dest
                    }
                }
            }
        }

        return null
    }

    private fun roomSeen(room: Room): Boolean {
        return explored.find { it.room == room } != null
    }

    private fun roomExplored(room: Room): Boolean {
        return explored.find { it.room == room && it.explored } != null
    }
}
