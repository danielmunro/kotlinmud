package kotlinmud.saver

import java.io.File
import kotlinmud.saver.mapper.mapDoor
import kotlinmud.saver.mapper.mapItem
import kotlinmud.saver.mapper.mapMob
import kotlinmud.saver.mapper.mapRoom
import kotlinmud.world.World

class WorldSaver(private val world: World) {
    fun save() {
        saveMobs()
        saveRooms()
        saveDoors()
        saveItems()
    }

    private fun saveMobs() {
        val file = File("areas/mobs.txt")
        val buffer = world.mobs.toList().joinToString("\n") {
            mapMob(it)
        }
        file.writeText(buffer)
    }

    private fun saveRooms() {
        val file = File("areas/rooms.txt")
        val buffer = world.rooms.toList().joinToString("\n") {
            mapRoom(it)
        }
        file.writeText(buffer)
    }

    private fun saveDoors() {
        val file = File("areas/doors.txt")
        val buffer = world.doors.toList().joinToString("") {
            mapDoor(it)
        }
        file.writeText(buffer)
    }

    private fun saveItems() {
        val file = File("areas/items.txt")
        val buffer = world.items.toList().joinToString("") {
            mapItem(it)
        }
        file.writeText(buffer)
    }
}