package kotlinmud.persistence.dumper

import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService
import java.io.File

class AreaDumperService(
    private val roomService: RoomService,
    private val mobService: MobService,
    private val itemService: ItemService,
) {
    fun dump() {
        roomService.getAllAreas().forEach { area ->
            val roomDumperService = RoomDumperService(area, roomService.findRoomModels(area))
            val mobDumperService = MobDumperService(area, mobService.findMobModels(area))
            val itemDumperService = ItemDumperService(area, itemService.findItemModels(area))
            File("world/${area.name}.txt").writeText(
                """area:
${area.id}. ${area.name}
lighting ${area.lighting}~
~

${roomDumperService.dump()}

${mobDumperService.dump()}

${itemDumperService.dump()}"""
            )
        }
    }
}
