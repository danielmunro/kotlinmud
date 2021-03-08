package kotlinmud.mob.path

import kotlinmud.room.model.Room

class Pathfinder(private val src: Room, private val dest: Room) {
    private val explored = mutableListOf<Room>()

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
            if (!roomSeen(it)) {
                explored.add(it)
                it.getAllExits().forEach { exit ->
                    val roomsToProceed = rooms.toMutableList()
                    roomsToProceed.add(exit.value)
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
        return explored.find { it == room } != null
    }
}
