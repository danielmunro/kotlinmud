package kotlinmud.loader

import java.io.File
import kotlinmud.item.Item
import kotlinmud.loader.loader.*
import kotlinmud.loader.model.*
import kotlinmud.loader.mapper.DoorMapper
import kotlinmud.loader.mapper.ItemMapper
import kotlinmud.loader.mapper.MobMapper
import kotlinmud.loader.mapper.RoomMapper
import kotlinmud.mob.Mob
import kotlinmud.room.exit.Door

class AreaLoader(private val baseDir: String) {
    fun load(): Area {
        return Area(
            RoomMapper(loadRooms(), loadDoors()).map(),
            loadItems(),
            loadMobs()
        )
    }

    private fun loadRooms(): List<RoomModel> {
        val roomLoader = RoomLoader(createTokenizer("$baseDir/rooms.txt"))
        val models: MutableList<RoomModel> = mutableListOf()
        addModels(roomLoader, models)
        return models.toList()
    }

    private fun loadDoors(): List<Door> {
        val doorLoader = DoorLoader(createTokenizer("$baseDir/doors.txt"))
        val models: MutableList<DoorModel> = mutableListOf()
        addModels(doorLoader, models)
        return DoorMapper(models.toList()).map()
    }

    private fun loadItems(): List<Item> {
        val itemLoader = ItemLoader(createTokenizer("$baseDir/items.txt"))
        val models: MutableList<ItemModel> = mutableListOf()
        addModels(itemLoader, models)
        return ItemMapper(models.toList()).map()
    }

    private fun loadMobs(): List<Mob> {
        val mobLoader = MobLoader(createTokenizer("$baseDir/mobs.txt"))
        val models: MutableList<MobModel> = mutableListOf()
        addModels(mobLoader, models)
        return MobMapper(models.toList()).map()
    }

    private fun createTokenizer(filename: String): Tokenizer {
        val classLoader = ClassLoader.getSystemClassLoader()
        return Tokenizer(File(classLoader.getResource(filename)!!.file).readText())
    }
}

fun <T> addModels(loader: Loader, models: MutableList<T>) {
    while (true) {
        try {
            @Suppress("UNCHECKED_CAST")
            models.add(loader.load() as T)
        } catch (e: Exception) {
            println(e)
            break
        }
    }
}
