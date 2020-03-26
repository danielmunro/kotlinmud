package kotlinmud.path

import kotlinmud.room.Room

class Pathfinder(private val src: Room, private val dest: Room) {
    private val explored: MutableList<Explored> = mutableListOf()

    fun find() {
        val found = proceed(mutableListOf(src), 0)
        if (found == null) {
            println("NOT FOUND")
            return
        }
        println("room found: ${found.size}")
        var move = 0
        found.forEach {
            move++
            println("$move -- ${it.name}")
        }
    }

    private fun proceed(rooms: MutableList<Room>, moves: Int): List<Room>? {
        println("proceed with $moves moves, ${rooms.size} rooms to examine, ${explored.size} explored")
        if (moves > 10) {
            return null
        }
        rooms.forEach {
            if (it == dest) {
                return rooms.toList()
            }

            println("checking ${it.name}")

            if (!roomExplored(it)) {
                println("${it.name} is not explored")
                if (!roomSeen(it)) {
                    println("not seen before, adding now")
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