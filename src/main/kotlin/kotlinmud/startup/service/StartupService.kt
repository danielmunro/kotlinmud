package kotlinmud.startup.service

import kotlinmud.helper.logger
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.startup.exception.RoomConnectionException
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.Parser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class StartupService(
    private val roomService: RoomService,
    private val mobService: MobService,
    private val itemService: ItemService,
    private val data: String? = null,
) {
    private val roomMap = mutableMapOf<Int, Room>()
    private val rooms = mutableListOf<RoomModel>()
    private val mobs = mutableListOf<MobModel>()
    private val items = mutableListOf<ItemModel>()
    private val mobRespawns = mutableListOf<MobRespawnModel>()
    private val itemRoomRespawns = mutableListOf<ItemRoomRespawnModel>()
    private val areas = mutableListOf<Area>()
    private val logger = logger(this)

    fun hydrateWorld() {
        logger.debug("hydrate world started")
        if (data == null) {
            readWorldSourceFiles()
        } else {
            generateModels(Parser(data).parse())
        }

        logger.debug("connect up rooms")
        connectUpRooms()

        logger.debug("respawn world")
        createRespawnService().also {
            it.respawn()
        }
    }

    private fun createRespawnService(): RespawnService {
        return RespawnService(
            mobs,
            items,
            mobRespawns,
            itemRoomRespawns,
            roomService,
            itemService,
            mobService,
        )
    }

    private fun connectUpRooms() {
        val connect = { startId: Int, endId: Int, direction: String ->
            val start = roomMap[startId]
            val end = roomMap[endId]
            if (start == null) {
                throw RoomConnectionException(startId)
            }
            if (end == null) {
                throw RoomConnectionException(endId)
            }
            when (direction) {
                "n" -> start.north = end
                "s" -> start.south = end
                "e" -> start.east = end
                "w" -> start.west = end
                "u" -> start.up = end
                "d" -> start.down = end
            }
        }
        rooms.forEach { model ->
            model.keywords.forEach {
                val keyword = it.key
                if (keyword == "n" ||
                    keyword == "s" ||
                    keyword == "e" ||
                    keyword == "w" ||
                    keyword == "u" ||
                    keyword == "d"
                ) {
                    connect(model.id, it.value.toInt(), keyword)
                }
            }
        }
    }

    private fun readWorldSourceFiles() {
        Files.list(Paths.get("./world")).forEach {
            logger.debug("parsing world file -- $it")
            generateModels(Parser(File(it.toUri()).readText()).parse())
        }
    }

    private fun generateModels(file: FileModel) {
        val area = Area.valueOf(file.area.name)
        file.rooms.forEach { model ->
            val room = RoomBuilder(roomService).also {
                it.area = area
                it.id = model.id
                it.name = model.name
                it.description = model.description
            }.build()
            roomMap[model.id] = room
            rooms.add(model)
            roomService.add(room)
        }
        mobRespawns.addAll(file.mobRespawns)
        itemRoomRespawns.addAll(file.itemRoomRespawns)
        mobs.addAll(file.mobs)
        items.addAll(file.items)
        areas.add(area)
    }
}
