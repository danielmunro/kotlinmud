package kotlinmud.persistence.dumper

import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import java.io.File

class AreaDumperService(private val roomService: RoomService) {
    fun dump() {
        var inc = 1
        Area.values().forEach { area ->
            val rooms = roomService.findByArea(area)
            val roomDumperService = RoomDumperService(rooms)
            File("world/${area.name}.txt").writeText(
                """area:
$inc. ${area.name}
${roomDumperService.dump()}"""
            )
            inc++
        }
    }
}
