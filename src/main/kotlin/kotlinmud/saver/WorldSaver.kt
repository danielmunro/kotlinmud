package kotlinmud.saver

import java.io.File
import kotlinmud.saver.mapper.mapDoor
import kotlinmud.saver.mapper.mapItem
import kotlinmud.saver.mapper.mapMob
import kotlinmud.saver.mapper.mapRoom
import kotlinmud.saver.mapper.reset.mapItemMobReset
import kotlinmud.saver.mapper.reset.mapItemRoomReset
import kotlinmud.saver.mapper.reset.mapMobReset
import kotlinmud.world.World

class WorldSaver(private val world: World) {
    fun save() {
        saveMobs()
        saveRooms()
        saveDoors()
        saveItems()
        saveMobResets()
        saveItemMobResets()
        saveItemRoomResets()
    }

    private fun saveMobs() {
        val file = File("state/areas/mobs.txt")
        val buffer = world.mobs.toList().joinToString("\n") {
            mapMob(it)
        }
        file.writeText(buffer)
    }

    private fun saveRooms() {
        val file = File("state/areas/rooms.txt")
        val buffer = world.rooms.toList().joinToString("\n") {
            mapRoom(it)
        }
        file.writeText(buffer)
    }

    private fun saveDoors() {
        val file = File("state/areas/doors.txt")
        val buffer = world.doors.toList().joinToString("") {
            mapDoor(it)
        }
        file.writeText(buffer)
    }

    private fun saveItems() {
        val file = File("state/areas/items.txt")
        val buffer = world.items.toList().joinToString("") {
            mapItem(it)
        }
        file.writeText(buffer)
    }

    private fun saveMobResets() {
        val file = File("state/areas/reset/mobs.txt")
        val buffer = world.mobResets.toList().joinToString("\n") {
            mapMobReset(it)
        }
        file.writeText(buffer)
    }

    private fun saveItemMobResets() {
        val file = File("state/areas/reset/item_mobs.txt")
        val buffer = world.itemMobResets.toList().joinToString("\n") {
            mapItemMobReset(it)
        }
        file.writeText(buffer)
    }

    private fun saveItemRoomResets() {
        val file = File("state/areas/reset/item_rooms.txt")
        val buffer = world.itemRoomResets.toList().joinToString("\n") {
            mapItemRoomReset(it)
        }
        file.writeText(buffer)
    }
}
