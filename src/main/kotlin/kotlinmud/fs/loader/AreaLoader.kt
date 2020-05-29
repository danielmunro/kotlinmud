package kotlinmud.fs.loader

import java.io.EOFException
import java.io.File
import kotlinmud.fs.loader.area.loader.DoorLoader
import kotlinmud.fs.loader.area.loader.ItemLoader
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.loader.MobLoader
import kotlinmud.fs.loader.area.loader.RoomLoader
import kotlinmud.fs.loader.area.loader.reset.ItemMobResetLoader
import kotlinmud.fs.loader.area.loader.reset.ItemRoomResetLoader
import kotlinmud.fs.loader.area.loader.reset.MobResetLoader
import kotlinmud.fs.loader.area.mapper.DoorMapper
import kotlinmud.fs.loader.area.mapper.ItemMapper
import kotlinmud.fs.loader.area.mapper.MobMapper
import kotlinmud.fs.loader.area.mapper.RoomMapper
import kotlinmud.fs.loader.area.model.reset.ItemMobReset
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset
import kotlinmud.fs.loader.area.model.reset.MobReset
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.world.Area
import kotlinmud.world.room.exit.Door

class AreaLoader(
    private val baseDir: String,
    private val loadSchemaVersion: Int
) {
    val mobs = loadMobs()
    fun load(): Area {
        return Area(
            createRoomMapper(mobs),
            loadItems(),
            mobs,
            loadMobResets(),
            loadItemMobResets(),
            loadItemRoomResets()
        )
    }

    private fun createRoomMapper(mobs: List<Mob>): RoomMapper {
        return RoomMapper(
            mobs,
            createModelList(RoomLoader(createTokenizer("$baseDir/rooms.txt"), loadSchemaVersion)),
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
            createModelList(MobLoader(createTokenizer("$baseDir/mobs.txt"), loadSchemaVersion))
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
