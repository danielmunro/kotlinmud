package kotlinmud.loader

import java.io.File
import kotlinmud.item.Item
import kotlinmud.loader.loader.*
import kotlinmud.loader.mapper.DoorMapper
import kotlinmud.loader.mapper.ItemMapper
import kotlinmud.loader.mapper.MobMapper
import kotlinmud.loader.mapper.RoomMapper
import kotlinmud.mob.Mob
import kotlinmud.room.Room
import kotlinmud.room.exit.Door

class AreaLoader(private val baseDir: String) {
    fun load(): Area {
        return Area(
            loadRooms(),
            loadItems(),
            loadMobs()
        )
    }

    private fun loadRooms(): List<Room> {
        return RoomMapper(
            createModelList(RoomLoader(createTokenizer("$baseDir/rooms.txt"))),
            loadDoors()
        ).map()
    }

    private fun loadDoors(): List<Door> {
        return DoorMapper(
            createModelList(DoorLoader(createTokenizer("$baseDir/doors.txt")))
        ).map()
    }

    private fun loadItems(): List<Item> {
        return ItemMapper(
            createModelList(ItemLoader(createTokenizer("$baseDir/items.txt")))
        ).map()
    }

    private fun loadMobs(): List<Mob> {
        return MobMapper(
            createModelList(MobLoader(createTokenizer("$baseDir/mobs.txt")))
        ).map()
    }

    private fun createTokenizer(filename: String): Tokenizer {
        val classLoader = ClassLoader.getSystemClassLoader()
        return Tokenizer(File(classLoader.getResource(filename)!!.file).readText())
    }
}

fun <T> createModelList(loader: Loader): List<T> {
    val models: MutableList<T> = mutableListOf()
    while (true) {
        try {
            @Suppress("UNCHECKED_CAST")
            models.add(loader.load() as T)
        } catch (e: Exception) {
            println(e)
            break
        }
    }
    return models.toList()
}
