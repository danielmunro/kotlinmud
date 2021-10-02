package kotlinmud.startup.service

import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.startup.parser.Parser

class StartupService(private val roomService: RoomService) {
    fun hydrateWorld() {
        val parser = Parser("./world/lorimir-forest.txt")
        val file = parser.parseFile()
        val roomMap = mutableMapOf<Int, Room>()
        val area = Area.valueOf(file.area.name)
        file.rooms.forEach { model ->
            val room = RoomBuilder(roomService).also {
                it.area = area
                it.id = model.id
                it.name = model.name
                it.description = model.description
            }.build()
            roomMap[model.id] = room
            roomService.add(room)
        }
        val connect = { startId: Int, endId: Int, direction: String ->
            println("connecting $startId to $endId")
            val start = roomMap[startId]
            val end = roomMap[endId]
            when (direction) {
                "n" -> start!!.north = end
                "s" -> start!!.south = end
                "e" -> start!!.east = end
                "w" -> start!!.west = end
                "u" -> start!!.up = end
                "d" -> start!!.down = end
            }
        }
        file.rooms.forEach { model ->
            model.keywords.forEach {
                val keyword = it.key
                if (keyword == "n" || keyword == "s" || keyword == "e" || keyword == "w" || keyword == "u" || keyword == "d") {
                    connect(model.id, it.value.toInt(), keyword)
                }
            }
        }
    }
}
