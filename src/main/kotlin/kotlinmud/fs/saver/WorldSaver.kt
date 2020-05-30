package kotlinmud.fs.saver

import java.io.File
import kotlinmud.fs.saver.mapper.mapMob
import kotlinmud.fs.saver.mapper.reset.mapItemMobReset
import kotlinmud.fs.saver.mapper.reset.mapItemRoomReset
import kotlinmud.fs.saver.mapper.reset.mapMobReset
import kotlinmud.item.mapper.mapItem
import kotlinmud.room.mapper.mapDoor
import kotlinmud.room.mapper.mapRoom
import kotlinmud.world.World

const val BASE_DIR = "state"
const val BASE_AREAS_DIR = "$BASE_DIR/bootstrap_world"

class WorldSaver(private val world: World) {
    fun save() {
        checkDirectoryExistence()
        saveMobs()
        saveRooms()
        saveDoors()
        saveItems()
        saveMobResets()
        saveItemMobResets()
        saveItemRoomResets()
    }

    private fun checkDirectoryExistence() {
        val base = File("$BASE_AREAS_DIR/reset")
        if (!base.exists()) {
            base.mkdirs()
        }
    }

    private fun saveMobs() {
        val file = File("$BASE_AREAS_DIR/mobs.txt")
        val buffer = world.mobs.toList().joinToString("\n") {
            mapMob(it)
        }
        file.writeText(buffer)
    }

    private fun saveRooms() {
        val file = File("$BASE_AREAS_DIR/rooms.txt")
        val buffer = world.rooms.toList().joinToString("\n") {
            mapRoom(it)
        }
        file.writeText(buffer)
    }

    private fun saveDoors() {
        val file = File("$BASE_AREAS_DIR/doors.txt")
        val buffer = world.doors.toList().joinToString("") {
            mapDoor(it)
        }
        file.writeText(buffer)
    }

    private fun saveItems() {
        val file = File("$BASE_AREAS_DIR/items.txt")
        val buffer = world.items.toList().joinToString("") {
            mapItem(it)
        }
        file.writeText(buffer)
    }

    private fun saveMobResets() {
        val file = File("$BASE_AREAS_DIR/reset/mobs.txt")
        val buffer = world.mobResets.toList().joinToString("\n") {
            mapMobReset(it)
        }
        file.writeText(buffer)
    }

    private fun saveItemMobResets() {
        val file = File("$BASE_AREAS_DIR/reset/item_mobs.txt")
        val buffer = world.itemMobResets.toList().joinToString("\n") {
            mapItemMobReset(it)
        }
        file.writeText(buffer)
    }

    private fun saveItemRoomResets() {
        val file = File("$BASE_AREAS_DIR/reset/item_rooms.txt")
        val buffer = world.itemRoomResets.toList().joinToString("\n") {
            mapItemRoomReset(it)
        }
        file.writeText(buffer)
    }
}
