package kotlinmud.persistence.dumper

import kotlinmud.room.service.RoomService
import java.io.File

class AreaDumperService(private val roomService: RoomService) {
    fun dump() {
        var inc = 1
        roomService.getAllAreas().forEach { area ->
            val rooms = roomService.findByArea(area)
            val roomDumperService = RoomDumperService(rooms)
            File("world/${area.name}.txt").writeText(
                """area:
$inc. ${area.name}
lighting good~
~

${roomDumperService.dump()}"""
            )
            inc++
        }
    }
}
