package kotlinmud.loader

import java.io.EOFException
import java.io.File
import kotlinmud.item.Item
import kotlinmud.loader.loader.DoorLoader
import kotlinmud.loader.loader.ItemLoader
import kotlinmud.loader.loader.Loader
import kotlinmud.loader.loader.MobLoader
import kotlinmud.loader.loader.RoomLoader
import kotlinmud.loader.loader.reset.ItemMobResetLoader
import kotlinmud.loader.loader.reset.ItemRoomResetLoader
import kotlinmud.loader.loader.reset.MobResetLoader
import kotlinmud.loader.mapper.DoorMapper
import kotlinmud.loader.mapper.ItemMapper
import kotlinmud.loader.mapper.MobMapper
import kotlinmud.loader.mapper.RoomMapper
import kotlinmud.loader.model.reset.ItemMobReset
import kotlinmud.loader.model.reset.ItemRoomReset
import kotlinmud.loader.model.reset.MobReset
import kotlinmud.mob.Mob
import kotlinmud.world.Area
import kotlinmud.world.room.exit.Door

class AreaLoader(private val baseDir: String) {
    fun load(): Area {
        return Area(
            baseDir,
            createRoomMapper(),
            loadItems(),
            loadMobs(),
            loadMobResets(),
            loadItemMobResets(),
            loadItemRoomResets()
        )
    }

    private fun createRoomMapper(): RoomMapper {
        return RoomMapper(
            createModelList(RoomLoader(createTokenizer("$baseDir/rooms.txt"))),
            loadDoors()
        )
    }

    private fun loadItemRoomResets(): List<ItemRoomReset> {
        return createModelList(ItemRoomResetLoader(createTokenizer("$baseDir/reset/item_rooms.txt")))
    }

    private fun loadMobResets(): List<MobReset> {
        return createModelList(MobResetLoader(createTokenizer("$baseDir/reset/mobs.txt")))
    }

    private fun loadItemMobResets(): List<ItemMobReset> {
        return createModelList(ItemMobResetLoader(createTokenizer("$baseDir/reset/item_mobs.txt")))
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

        return Tokenizer(File(filename).readText())
    }
}

fun <T> createModelList(loader: Loader): List<T> {
    val models: MutableList<T> = mutableListOf()
    while (true) {
        try {
            @Suppress("UNCHECKED_CAST")
            models.add(loader.load() as T)
        } catch (e: EOFException) {
            break
        }
    }
    return models.toList()
}
