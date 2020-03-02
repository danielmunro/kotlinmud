package kotlinmud.loader

import java.io.File
import kotlinmud.item.Item
import kotlinmud.loader.loader.DoorLoader
import kotlinmud.loader.loader.ItemLoader
import kotlinmud.loader.loader.MobLoader
import kotlinmud.loader.loader.RoomLoader
import kotlinmud.loader.model.DoorModel
import kotlinmud.loader.model.ItemModel
import kotlinmud.loader.model.MobModel
import kotlinmud.loader.model.RoomModel
import kotlinmud.mapper.DoorMapper
import kotlinmud.mapper.ItemMapper
import kotlinmud.mapper.MobMapper
import kotlinmud.mapper.RoomMapper
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
        val filename = "$baseDir/rooms.txt"
        val tokenizer = createTokenizer(filename)
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
        val tokenizer = createTokenizer(filename)
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

    private fun loadItems(): List<Item> {
        val filename = "$baseDir/items.txt"
        val tokenizer = createTokenizer(filename)
        val itemLoader = ItemLoader(tokenizer)
        val models: MutableList<ItemModel> = mutableListOf()
        while (true) {
            try {
                models.add(itemLoader.load())
            } catch (e: Exception) {
                println(e)
                break
            }
        }
        return ItemMapper(models.toList()).map()
    }

    private fun loadMobs(): List<Mob> {
        val filename = "$baseDir/mobs.txt"
        val tokenizer = createTokenizer(filename)
        val mobLoader = MobLoader(tokenizer)
        val models: MutableList<MobModel> = mutableListOf()
        while (true) {
            try {
                models.add(mobLoader.load())
            } catch (e: Exception) {
                println(e)
                break
            }
        }
        return MobMapper(models.toList()).map()
    }
}

fun createTokenizer(filename: String): Tokenizer {
    val classLoader = ClassLoader.getSystemClassLoader()
    return Tokenizer(File(classLoader.getResource(filename)!!.file).readText())
}
