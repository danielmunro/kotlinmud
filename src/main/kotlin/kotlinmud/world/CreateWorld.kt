package kotlinmud.world

import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.room.service.RoomService
import kotlinmud.world.itrias.fusil.createFusilOutskirts
import kotlinmud.world.itrias.lorimir.createLorimirForest
import kotlinmud.world.itrias.lorimir.createLorimirForestLake
import kotlinmud.world.itrias.lorimir.createLorimirForestOutpost

fun createWorld(mobService: MobService, itemService: ItemService, roomService: RoomService) {
    val lorimirOutpost = createLorimirForestOutpost(mobService, itemService, roomService)
    val outskirtsConnection = createFusilOutskirts(roomService, lorimirOutpost)
    val lorimirForest = createLorimirForest(mobService, roomService, lorimirOutpost)
    createLorimirForestLake(roomService, lorimirForest)
}
