package kotlinmud.startup.service

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.startup.exception.RoomConnectionException
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.Parser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class StartupService(private val roomService: RoomService) {
    private val roomMap = mutableMapOf<Int, Room>()
    private val roomModels = mutableListOf<RoomModel>()

    fun hydrateWorld() {
        println("hydrate world started")
        readWorldSourceFiles()
        connectUpRooms()
        println("world hydration done -- ${roomModels.size} rooms")
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
        roomModels.forEach { model ->
            model.keywords.forEach {
                val keyword = it.key
                if (keyword == "n" || keyword == "s" || keyword == "e" || keyword == "w" || keyword == "u" || keyword == "d") {
                    connect(model.id, it.value.toInt(), keyword)
                }
            }
        }
    }

    private fun readWorldSourceFiles() {
        Files.list(Paths.get("./world")).forEach {
            println(it)
            generateModels(Parser(File(it.toUri())).parseFile())
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
            roomModels.add(model)
            roomService.add(room)
        }
    }
}
