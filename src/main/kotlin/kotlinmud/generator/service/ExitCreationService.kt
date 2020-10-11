package kotlinmud.generator.service

import kotlinmud.generator.constant.DEPTH
import kotlinmud.generator.model.World
import org.jetbrains.exposed.sql.transactions.transaction

class ExitCreationService(private val world: World) {
    fun hookUpRoomExits() {
        for (z in world.matrix3D.indices) {
            for (y in world.matrix3D[z].indices) {
                for (x in world.matrix3D[z][y].indices) {
                    transaction { hookUpRoom(world, x, y, z) }
                }
            }
        }
    }

    private fun hookUpRoom(world: World, x: Int, y: Int, z: Int) {
        val id = world.matrix3D[z][y][x]
        val room = world.rooms[id]
        if (world.matrix3D[z][y].size > x + 1) {
            val destId = world.matrix3D[z][y][x + 1]
            val dest = world.rooms[destId]
            room.east = dest
            dest.west = room
        }
        if (world.matrix3D[z].size > y + 1) {
            val destId = world.matrix3D[z][y + 1][x]
            val dest = world.rooms[destId]
            room.south = dest
            dest.north = room
        }
        if (z + 1 < DEPTH) {
            val destId = world.matrix3D[z + 1][y][x]
            val dest = world.rooms[destId]
            room.up = dest
            dest.down = room
        }
    }
}
