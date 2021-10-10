package kotlinmud

import kotlinmud.app.createContainer
import kotlinmud.helper.getAllDataFiles
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.quest.helper.questList
import kotlinmud.room.service.RoomService
import kotlinmud.startup.service.StartupService
import org.kodein.di.erased.instance

fun main() {
    val data = getAllDataFiles()
    val container = createContainer(0)
    val roomService by container.instance<RoomService>()
    val mobService by container.instance<MobService>()
    val itemService by container.instance<ItemService>()
    val svc = StartupService(roomService, mobService, itemService, data)
    svc.hydrateWorld()

    println("--- world hydration successful ---")
    println("${roomService.getRoomCount()} rooms")
    println("${mobService.getMobCount()} mobs")
    println("${itemService.getItemCount()} items")
    println("${questList.size} quests")
}
