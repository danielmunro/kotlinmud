package kotlinmud.loader

import kotlinmud.loader.loader.DoorLoader
import kotlinmud.loader.loader.RoomLoader
import kotlinmud.loader.model.DoorModel
import kotlinmud.loader.model.RoomModel
import kotlinmud.mapper.DoorMapper
import kotlinmud.mapper.RoomMapper
import kotlinmud.room.Room
import kotlinmud.room.exit.Door
import java.io.File

class AreaLoader(private val baseDir: String) {
    fun load(): List<Room> {
        return RoomMapper(loadRooms(), loadDoors()).map()
    }

    private fun loadRooms(): List<RoomModel> {
        val filename = "$baseDir/rooms.txt"
        val classLoader = ClassLoader.getSystemClassLoader()
        val tokenizer = Tokenizer(File(classLoader.getResource(filename)!!.file).readText())
        val roomLoader = RoomLoader(tokenizer)
        val models: MutableList<RoomModel> = mutableListOf()
        while (true) {
            try {
                models.add(roomLoader.load())
            } catch (e: Exception) {
                break
            }
        }
        return models.toList()
    }

    private fun loadDoors(): List<Door> {
        val filename = "$baseDir/doors.txt"
        val classLoader = ClassLoader.getSystemClassLoader()
        val tokenizer = Tokenizer(File(classLoader.getResource(filename)!!.file).readText())
        val doorLoader = DoorLoader(tokenizer)
        val models: MutableList<DoorModel> = mutableListOf()
        while (true) {
            try {
                models.add(doorLoader.load())
            } catch (e: Exception) {
                println(e)
                break
            }
        }
        return DoorMapper(models.toList()).map()
    }
}