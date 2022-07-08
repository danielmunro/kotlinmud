package kotlinmud.persistence.dumper

import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService
import java.io.File

class AreaDumperService(
    private val roomService: RoomService,
    private val mobService: MobService,
) {
    fun dump() {
        var inc = 1
        roomService.getAllAreas().forEach { area ->
            val roomDumperService = RoomDumperService(roomService.findByArea(area))
            val mobDumperService = MobDumperService(area, mobService.findMobsToDump(area))
            File("world/${area.name}.txt").writeText(
                """area:
$inc. ${area.name}
lighting ${area.lighting}~
~

${roomDumperService.dump()}

${mobDumperService.dump()}"""
            )
            inc++
        }
    }
}
