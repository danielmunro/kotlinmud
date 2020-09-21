package kotlinmud.mob.path

import kotlinmud.room.dao.RoomDAO

class Pathfinder(private val src: RoomDAO, private val dest: RoomDAO) {
    private val explored = mutableListOf<Int>()

    fun find(): List<RoomDAO> {
        return proceed(mutableListOf(src), 0) ?: listOf()
    }

    private fun proceed(rooms: MutableList<RoomDAO>, moves: Int): List<RoomDAO>? {
        if (moves > 10) {
            return null
        }
        rooms.forEach {
            if (it == dest) {
                return rooms.toList()
            }
            if (!roomSeen(it)) {
                explored.add(it.id.value)
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

    private fun roomSeen(room: RoomDAO): Boolean {
        return explored.find { it == room.id.value } != null
    }
}
